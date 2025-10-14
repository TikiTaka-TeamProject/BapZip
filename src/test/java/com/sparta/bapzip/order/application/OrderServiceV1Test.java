package com.sparta.bapzip.order.application;

import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import com.sparta.bapzip.order.application.dto.*;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.order.application.exception.MenusNotFoundInOrderException;
import com.sparta.bapzip.order.application.exception.OrderNotFoundException;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.order.domain.exception.*;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.ordermenu.application.OrderMenuServiceV1;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderServiceV1 테스트")
class OrderServiceV1Test {

    @InjectMocks
    private OrderServiceV1 orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShopServiceV1 shopService;

    @Mock
    private MenuServiceV1 menuService;

    @Mock
    private OrderMenuServiceV1 orderMenuService;

    private UserEntity customer;
    private UserEntity owner;
    private ShopEntity shop;
    private MenuEntity menu1;
    private MenuEntity menu2;
    private OrderEntity order;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        // 고객 설정
        customer = UserEntity.builder()
                .id(1L)
                .name("customer")
                .build();

        // 가게 주인 설정
        owner = UserEntity.builder()
                .id(2L)
                .name("owner")
                .build();

        // 가게 설정
        shop = ShopEntity.builder()
                .id(UUID.randomUUID())
                .name("테스트 가게")
                .owner(owner)
                .build();

        // 메뉴 설정
        menu1 = MenuEntity.builder()
                .id(UUID.randomUUID())
                .name("메뉴1")
                .price(10000)
                .status(MenuStatus.AVAILABLE)
                .shop(shop)
                .build();

        menu2 = MenuEntity.builder()
                .id(UUID.randomUUID())
                .name("메뉴2")
                .price(15000)
                .status(MenuStatus.AVAILABLE)
                .shop(shop)
                .build();

        // 주문 요청 설정
        List<CreateOrderRequest.MenuInfo> menuInfoList = Arrays.asList(
                new CreateOrderRequest.MenuInfo(menu1.getId(), 2),
                new CreateOrderRequest.MenuInfo(menu2.getId(), 1)
        );

        createOrderRequest = CreateOrderRequest.builder()
                .shopId(shop.getId())
                .menuInfoList(menuInfoList)
                .deliveryAddress("서울시 강남구")
                .detailAddress("101호")
                .paymentType("CARD")
                .specialRequest("빨리 부탁드립니다")
                .build();

        // 주문 엔티티 설정
        order = OrderEntity.builder()
                .id(UUID.randomUUID())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .status(OrderStatus.PENDING)
                .deliveryAddress("서울시 강남구")
                .detailAddress("101호")
                .paymentType("CARD")
                .totalPrice(35000)
                .specialRequests("빨리 부탁드립니다")
                .user(customer)
                .build();
    }

    @Nested
    @DisplayName("주문 생성 테스트")
    class CreateOrderTest {

        @Test
        @DisplayName("정상적으로 주문이 생성된다")
        void createOrder_Success() {
            // given
            when(shopService.getShopById(any())).thenReturn(shop);
            when(menuService.getMenusByIds(anyList())).thenReturn(Arrays.asList(menu1, menu2));
            when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);
            when(orderMenuService.saveAll(anyList())).thenReturn(Collections.emptyList());

            // when
            OrderCreationDto result = orderService.createOrder(createOrderRequest, customer);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getShopName()).isEqualTo(shop.getName());
            assertThat(result.getTotalPrice()).isEqualTo(35000);
            verify(orderRepository, times(1)).save(any(OrderEntity.class));
            verify(orderMenuService, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("존재하지 않는 메뉴가 포함되면 예외가 발생한다")
        void createOrder_MenuNotFound() {
            // given
            when(shopService.getShopById(any())).thenReturn(shop);
            when(menuService.getMenusByIds(anyList())).thenReturn(Collections.singletonList(menu1)); // 1개만 반환

            // when & then
            assertThatThrownBy(() -> orderService.createOrder(createOrderRequest, customer))
                    .isInstanceOf(MenusNotFoundInOrderException.class);
        }

        @Test
        @DisplayName("품절된 메뉴가 포함되면 예외가 발생한다")
        void createOrder_SoldOutMenu() {
            // given
            MenuEntity soldOutMenu = MenuEntity.builder()
                    .id(menu1.getId())
                    .name("품절 메뉴")
                    .price(10000)
                    .status(MenuStatus.SOLD_OUT)
                    .shop(shop)
                    .build();

            when(shopService.getShopById(any())).thenReturn(shop);
            when(menuService.getMenusByIds(anyList())).thenReturn(Arrays.asList(soldOutMenu, menu2));

            // when & then
            assertThatThrownBy(() -> orderService.createOrder(createOrderRequest, customer))
                    .isInstanceOf(SoldOutMenuException.class);
        }

        @Test
        @DisplayName("다른 가게의 메뉴가 포함되면 예외가 발생한다")
        void createOrder_MenuNotInShop() {
            // given
            ShopEntity otherShop = ShopEntity.builder()
                    .id(UUID.randomUUID())
                    .name("다른 가게")
                    .owner(owner)
                    .build();

            MenuEntity menuFromOtherShop = MenuEntity.builder()
                    .id(menu2.getId())
                    .name("다른 가게 메뉴")
                    .price(15000)
                    .status(MenuStatus.AVAILABLE)
                    .shop(otherShop)
                    .build();

            when(shopService.getShopById(any())).thenReturn(shop);
            when(menuService.getMenusByIds(anyList())).thenReturn(Arrays.asList(menu1, menuFromOtherShop));

            // when & then
            assertThatThrownBy(() -> orderService.createOrder(createOrderRequest, customer))
                    .isInstanceOf(MenuNotInShopException.class);
        }
    }

    @Nested
    @DisplayName("주문 조회 테스트")
    class GetOrderTest {

        @Test
        @DisplayName("주문 ID로 정상적으로 주문을 조회한다")
        void getOrderById_Success() {
            // given
            UUID orderId = order.getId();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when
            OrderDetailDto result = orderService.getOrderById(orderId, customer);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getShopName()).isEqualTo(order.getShopName());
            verify(orderRepository, times(1)).findById(orderId);
        }

        @Test
        @DisplayName("존재하지 않는 주문 ID로 조회 시 예외가 발생한다")
        void getOrderById_NotFound() {
            // given
            UUID orderId = UUID.randomUUID();
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.getOrderById(orderId, customer))
                    .isInstanceOf(OrderNotFoundException.class);
        }

        @Test
        @DisplayName("다른 사용자의 주문을 조회하면 예외가 발생한다")
        void getOrderById_ForbiddenAccess() {
            // given
            UUID orderId = order.getId();
            UserEntity otherUser = UserEntity.builder()
                    .id(999L)
                    .name("otherUser")
                    .build();

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.getOrderById(orderId, otherUser))
                    .isInstanceOf(ForbiddenOrderAccessException.class);
        }

        @Test
        @DisplayName("사용자의 주문 목록을 페이징하여 조회한다")
        void getOrdersByUser_Success() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            List<OrderEntity> orders = Collections.singletonList(order);
            Page<OrderEntity> orderPage = new PageImpl<>(orders, pageable, orders.size());

            when(orderRepository.findOrderByUser(customer, pageable)).thenReturn(orderPage);

            // when
            Page<OrderDto> result = orderService.getOrdersByUser(customer, pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(orderRepository, times(1)).findOrderByUser(customer, pageable);
        }

        @Test
        @DisplayName("가게의 주문 목록을 페이징하여 조회한다")
        void getOrderByShopId_Success() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            List<OrderEntity> orders = Collections.singletonList(order);
            Page<OrderEntity> orderPage = new PageImpl<>(orders, pageable, orders.size());

            when(shopService.getShopById(shop.getId())).thenReturn(shop);
            when(orderRepository.findOrderByShopId(shop.getId(), pageable)).thenReturn(orderPage);
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            Page<ShopOrderDto> result = orderService.getOrderByShopId(shop.getId(), owner, pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(orderRepository, times(1)).findOrderByShopId(shop.getId(), pageable);
        }
    }

    @Nested
    @DisplayName("주문 삭제 테스트")
    class DeleteOrderTest {

        @Test
        @DisplayName("정상적으로 주문을 삭제한다")
        void deleteOrder_Success() {
            // given
            UUID orderId = order.getId();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when
            orderService.delete(orderId, customer);

            // then
            verify(orderRepository, times(1)).findById(orderId);
        }

        @Test
        @DisplayName("다른 사용자는 주문을 삭제할 수 없다")
        void deleteOrder_ForbiddenAccess() {
            // given
            UUID orderId = order.getId();
            UserEntity otherUser = UserEntity.builder()
                    .id(999L)
                    .name("otherUser")
                    .build();

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.delete(orderId, otherUser))
                    .isInstanceOf(ForbiddenOrderAccessException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트 - 사장님")
    class OwnerOrderStatusTest {

        @Test
        @DisplayName("주문을 수락한다")
        void acceptOrder_Success() {
            // given
            UUID orderId = order.getId();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            orderService.acceptOrder(orderId, owner);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
        }

        @Test
        @DisplayName("대기 중이 아닌 주문은 수락할 수 없다")
        void acceptOrder_NotPending() {
            // given
            UUID orderId = order.getId();
            order.accept(); // 이미 수락된 상태로 변경

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when & then
            assertThatThrownBy(() -> orderService.acceptOrder(orderId, owner))
                    .isInstanceOf(OrderNotPendingException.class);
        }

        @Test
        @DisplayName("주문을 거절한다")
        void rejectOrder_Success() {
            // given
            UUID orderId = order.getId();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            orderService.rejectOrder(orderId, owner);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.REJECTED);
        }

        @Test
        @DisplayName("조리를 시작한다")
        void startCooking_Success() {
            // given
            UUID orderId = order.getId();
            order.accept(); // 먼저 수락 상태로 변경
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            orderService.startCooking(orderId, owner);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COOKING);
        }

        @Test
        @DisplayName("수락되지 않은 주문은 조리를 시작할 수 없다")
        void startCooking_NotAccepted() {
            // given
            UUID orderId = order.getId();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when & then
            assertThatThrownBy(() -> orderService.startCooking(orderId, owner))
                    .isInstanceOf(OrderNotAcceptedException.class);
        }

        @Test
        @DisplayName("조리를 완료한다")
        void completeCooking_Success() {
            // given
            UUID orderId = order.getId();
            order.accept();
            order.startCooking();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            orderService.completeCooking(orderId, owner);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COOK_COMPLETED);
        }

        @Test
        @DisplayName("배달을 시작한다")
        void startDelivery_Success() {
            // given
            UUID orderId = order.getId();
            order.accept();
            order.startCooking();
            order.completeCooking();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            orderService.startDelivery(orderId, owner);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);
        }

        @Test
        @DisplayName("배달을 완료한다")
        void completeDelivery_Success() {
            // given
            UUID orderId = order.getId();
            order.accept();
            order.startCooking();
            order.completeCooking();
            order.startDelivery();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            doNothing().when(shopService).validateShopOwner(shop.getId(), owner.getId());

            // when
            orderService.completeDelivery(orderId, owner);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        }
    }

    @Nested
    @DisplayName("주문 취소 테스트 - 고객")
    class CustomerOrderCancelTest {

        @Test
        @DisplayName("대기 중인 주문을 취소한다")
        void cancelOrder_Success() {
            // given
            UUID orderId = order.getId();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when
            orderService.cancelOrder(orderId, customer);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        }

        @Test
        @DisplayName("대기 중이 아닌 주문은 취소할 수 없다")
        void cancelOrder_NotCancellable() {
            // given
            UUID orderId = order.getId();
            order.accept(); // 수락된 상태로 변경
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(orderId, customer))
                    .isInstanceOf(OrderNotCancellableException.class);
        }

        @Test
        @DisplayName("다른 사용자는 주문을 취소할 수 없다")
        void cancelOrder_ForbiddenAccess() {
            // given
            UUID orderId = order.getId();
            UserEntity otherUser = UserEntity.builder()
                    .id(999L)
                    .name("otherUser")
                    .build();

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(orderId, otherUser))
                    .isInstanceOf(ForbiddenOrderAccessException.class);
        }
    }
}