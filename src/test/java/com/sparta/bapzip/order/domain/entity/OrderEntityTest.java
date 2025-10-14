package com.sparta.bapzip.order.domain.entity;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.order.domain.exception.*;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OrderEntity 도메인 테스트")
class OrderEntityTest {

    private UserEntity customer;
    private UserEntity owner;
    private ShopEntity shop;
    private MenuEntity menu1;
    private MenuEntity menu2;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        customer = UserEntity.builder()
                .id(1L)
                .name("customer")
                .build();

        owner = UserEntity.builder()
                .id(2L)
                .name("owner")
                .build();

        shop = ShopEntity.builder()
                .id(UUID.randomUUID())
                .name("테스트 가게")
                .owner(owner)
                .build();

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
    }

    @Nested
    @DisplayName("주문 생성 테스트")
    class CreateOrderTest {

        @Test
        @DisplayName("정상적으로 주문이 생성된다")
        void create_Success() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            // when
            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            // then
            assertThat(order).isNotNull();
            assertThat(order.getShopId()).isEqualTo(shop.getId());
            assertThat(order.getShopName()).isEqualTo(shop.getName());
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(order.getTotalPrice()).isEqualTo(35000); // (10000 * 2) + (15000 * 1)
            assertThat(order.getDeliveryAddress()).isEqualTo("서울시 강남구");
            assertThat(order.getDetailAddress()).isEqualTo("101호");
            assertThat(order.getPaymentType()).isEqualTo("CARD");
            assertThat(order.getUser()).isEqualTo(customer);
        }

        @Test
        @DisplayName("다른 가게의 메뉴가 포함되면 예외가 발생한다")
        void create_MenuNotInShop() {
            // given
            ShopEntity otherShop = ShopEntity.builder()
                    .id(UUID.randomUUID())
                    .name("다른 가게")
                    .owner(owner)
                    .build();

            MenuEntity menuFromOtherShop = MenuEntity.builder()
                    .id(UUID.randomUUID())
                    .name("다른 가게 메뉴")
                    .price(20000)
                    .status(MenuStatus.AVAILABLE)
                    .shop(otherShop)
                    .build();

            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menuFromOtherShop.getId(), menuFromOtherShop);

            // when & then
            assertThatThrownBy(() -> OrderEntity.create(createOrderRequest, customer, shop, menuMap))
                    .isInstanceOf(MenuNotInShopException.class);
        }

        @Test
        @DisplayName("품절된 메뉴가 포함되면 예외가 발생한다")
        void create_SoldOutMenu() {
            // given
            MenuEntity soldOutMenu = MenuEntity.builder()
                    .id(menu1.getId())
                    .name("품절 메뉴")
                    .price(10000)
                    .status(MenuStatus.SOLD_OUT)
                    .shop(shop)
                    .build();

            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(soldOutMenu.getId(), soldOutMenu);
            menuMap.put(menu2.getId(), menu2);

            // when & then
            assertThatThrownBy(() -> OrderEntity.create(createOrderRequest, customer, shop, menuMap))
                    .isInstanceOf(SoldOutMenuException.class);
        }

        @Test
        @DisplayName("총 가격이 정확하게 계산된다")
        void create_TotalPriceCalculation() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            // when
            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            // then
            int expectedTotal = (menu1.getPrice() * 2) + (menu2.getPrice() * 1);
            assertThat(order.getTotalPrice()).isEqualTo(expectedTotal);
        }
    }

    @Nested
    @DisplayName("고객 권한 검증 테스트")
    class ValidateCustomerTest {

        @Test
        @DisplayName("주문한 고객 본인은 검증을 통과한다")
        void validateCustomer_Success() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            // when & then
            assertThatCode(() -> order.validateCustomer(customer.getId()))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("다른 사용자는 검증에 실패한다")
        void validateCustomer_Forbidden() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            Long otherUserId = 999L;

            // when & then
            assertThatThrownBy(() -> order.validateCustomer(otherUserId))
                    .isInstanceOf(ForbiddenOrderAccessException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class OrderStatusChangeTest {

        private OrderEntity order;

        @BeforeEach
        void setUp() {
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);
        }

        @Test
        @DisplayName("대기 중인 주문을 수락한다")
        void accept_Success() {
            // when
            order.accept();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
        }

        @Test
        @DisplayName("대기 중이 아닌 주문은 수락할 수 없다")
        void accept_NotPending() {
            // given
            order.accept(); // 이미 수락된 상태

            // when & then
            assertThatThrownBy(() -> order.accept())
                    .isInstanceOf(OrderNotPendingException.class);
        }

        @Test
        @DisplayName("대기 중인 주문을 거절한다")
        void reject_Success() {
            // when
            order.reject();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.REJECTED);
        }

        @Test
        @DisplayName("대기 중이 아닌 주문은 거절할 수 없다")
        void reject_NotPending() {
            // given
            order.accept();

            // when & then
            assertThatThrownBy(() -> order.reject())
                    .isInstanceOf(OrderNotPendingException.class);
        }

        @Test
        @DisplayName("수락된 주문의 조리를 시작한다")
        void startCooking_Success() {
            // given
            order.accept();

            // when
            order.startCooking();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COOKING);
        }

        @Test
        @DisplayName("수락되지 않은 주문은 조리를 시작할 수 없다")
        void startCooking_NotAccepted() {
            // when & then
            assertThatThrownBy(() -> order.startCooking())
                    .isInstanceOf(OrderNotAcceptedException.class);
        }

        @Test
        @DisplayName("조리 중인 주문을 완료한다")
        void completeCooking_Success() {
            // given
            order.accept();
            order.startCooking();

            // when
            order.completeCooking();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COOK_COMPLETED);
        }

        @Test
        @DisplayName("조리 중이 아닌 주문은 조리 완료할 수 없다")
        void completeCooking_NotCooking() {
            // given
            order.accept();

            // when & then
            assertThatThrownBy(() -> order.completeCooking())
                    .isInstanceOf(OrderNotCookingException.class);
        }

        @Test
        @DisplayName("조리 완료된 주문의 배달을 시작한다")
        void startDelivery_Success() {
            // given
            order.accept();
            order.startCooking();
            order.completeCooking();

            // when
            order.startDelivery();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);
        }

        @Test
        @DisplayName("조리 완료되지 않은 주문은 배달을 시작할 수 없다")
        void startDelivery_NotCookCompleted() {
            // given
            order.accept();
            order.startCooking();

            // when & then
            assertThatThrownBy(() -> order.startDelivery())
                    .isInstanceOf(OrderNotCookCompletedException.class);
        }

        @Test
        @DisplayName("배달 중인 주문을 완료한다")
        void completeDelivery_Success() {
            // given
            order.accept();
            order.startCooking();
            order.completeCooking();
            order.startDelivery();

            // when
            order.completeDelivery();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        }

        @Test
        @DisplayName("배달 중이 아닌 주문은 배달 완료할 수 없다")
        void completeDelivery_NotDelivering() {
            // given
            order.accept();
            order.startCooking();
            order.completeCooking();

            // when & then
            assertThatThrownBy(() -> order.completeDelivery())
                    .isInstanceOf(OrderNotDeliveringException.class);
        }

        @Test
        @DisplayName("대기 중인 주문을 취소한다")
        void cancel_Success() {
            // when
            order.cancel();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        }

        @Test
        @DisplayName("대기 중이 아닌 주문은 취소할 수 없다")
        void cancel_NotCancellable() {
            // given
            order.accept();

            // when & then
            assertThatThrownBy(() -> order.cancel())
                    .isInstanceOf(OrderNotCancellableException.class);
        }

        @Test
        @DisplayName("주문 상태 흐름이 정상적으로 진행된다")
        void orderStatusFlow_Success() {
            // 초기 상태: PENDING
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

            // PENDING -> ACCEPTED
            order.accept();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.ACCEPTED);

            // ACCEPTED -> COOKING
            order.startCooking();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COOKING);

            // COOKING -> COOK_COMPLETED
            order.completeCooking();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COOK_COMPLETED);

            // COOK_COMPLETED -> DELIVERING
            order.startDelivery();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);

            // DELIVERING -> DELIVERED
            order.completeDelivery();
            assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        }
    }

    @Nested
    @DisplayName("주문 삭제 테스트")
    class DeleteOrderTest {

        @Test
        @DisplayName("주문을 삭제한다")
        void delete_Success() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            // when
            order.delete(customer.getId());

            // then
            assertThat(order.getDeletedAt()).isNotNull();
            assertThat(order.getDeletedBy()).isEqualTo(customer.getId());
        }
    }

    @Nested
    @DisplayName("연관관계 설정 테스트")
    class AddOrderMenuTest {

        @Test
        @DisplayName("주문 메뉴를 정상적으로 추가한다")
        void addOrderMenu_Success() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            OrderMenuEntity orderMenu = OrderMenuEntity.builder()
                    .id(UUID.randomUUID())
                    .menu(menu1)
                    .name(menu1.getName())
                    .quantity(2)
                    .price(menu1.getPrice())
                    .build();

            // when
            order.addOrderMenu(orderMenu);

            // then
            assertThat(order.getOrderMenuList()).hasSize(1);
            assertThat(order.getOrderMenuList()).contains(orderMenu);
        }

        @Test
        @DisplayName("여러 주문 메뉴를 추가한다")
        void addOrderMenu_Multiple() {
            // given
            Map<UUID, MenuEntity> menuMap = new HashMap<>();
            menuMap.put(menu1.getId(), menu1);
            menuMap.put(menu2.getId(), menu2);

            OrderEntity order = OrderEntity.create(createOrderRequest, customer, shop, menuMap);

            OrderMenuEntity orderMenu1 = OrderMenuEntity.builder()
                    .id(UUID.randomUUID())
                    .menu(menu1)
                    .name(menu1.getName())
                    .quantity(2)
                    .price(menu1.getPrice())
                    .build();

            OrderMenuEntity orderMenu2 = OrderMenuEntity.builder()
                    .id(UUID.randomUUID())
                    .menu(menu2)
                    .name(menu2.getName())
                    .quantity(1)
                    .price(menu2.getPrice())
                    .build();

            // when
            order.addOrderMenu(orderMenu1);
            order.addOrderMenu(orderMenu2);

            // then
            assertThat(order.getOrderMenuList()).hasSize(2);
            assertThat(order.getOrderMenuList()).containsExactly(orderMenu1, orderMenu2);
        }
    }
}