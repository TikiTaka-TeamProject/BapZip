package com.sparta.bapzip.order.application;

import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.application.dto.OrderCreationDto;
import com.sparta.bapzip.order.application.dto.OrderDetailDto;
import com.sparta.bapzip.order.application.dto.ShopOrderDto;
import com.sparta.bapzip.order.domain.exception.ForbiddenOrderAccessException;
import com.sparta.bapzip.order.application.dto.OrderDto;
import com.sparta.bapzip.order.application.exception.MenusNotFoundInOrderException;
import com.sparta.bapzip.order.application.exception.OrderNotFoundException;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.ordermenu.application.OrderMenuServiceV1;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.payment.application.PaymentServiceV1;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sparta.bapzip.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class OrderServiceV1 {

    private final OrderRepository orderRepository;
    private final ShopServiceV1 shopService;
    private final MenuServiceV1 menuService;
    private final OrderMenuServiceV1 orderMenuService;
    private final PaymentServiceV1 paymentServiceV1;

    /**
     * 새로운 주문을 생성
     *
     * @param request 주문 요청 DTO (가게 ID, 메뉴 ID 및 수량 정보 포함)
     * @return 생성된 주문 응답(ResponseEntity)
     */
    public OrderCreationDto createOrder(@Valid CreateOrderRequest request, UserEntity user) {
        ShopEntity shop = shopService.getShopById(request.getShopId());
        Map<UUID, MenuEntity> menuMap = getMenuMap(request.getMenuInfoList());

        OrderEntity order = OrderEntity.create(request, user, shop, menuMap);
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderMenuEntity> orderMenuList = createOrderMenusFromRequest(
                request.getMenuInfoList(),
                menuMap,
                savedOrder
        );

        orderMenuList.forEach(savedOrder::addOrderMenu);

        orderMenuService.saveAll(orderMenuList);
        paymentServiceV1.createPayment(order.getId());
        return OrderCreationDto.from(savedOrder, orderMenuList, shop, user.getId());
    }


    /**
     * 본인의 주문 중 한 건 상세 정보 조회
     *
     * @param orderId 조회할 주문의 고유 ID (UUID)
     * @param user 주문의 소유 여부를 확인할 현재 사용자 정보
     * @throws OrderNotFoundException 주문 ID에 해당하는 주문이 데이터베이스에 없을 경우 발생
     * @throws ForbiddenOrderAccessException 조회된 주문의 소유자 ID와 현재 사용자 ID가 일치하지 않을 경우 발생
     */
    @Transactional(readOnly = true)
    public OrderDetailDto getOrderById(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        order.validateCustomer(user.getId());

        return OrderDetailDto.from(order);
    }

    /**
     * 본인의 주문내역을 조회한다.
     *
     * @param user 본인의 주문을 확인하기 위한 정보
     * @param pageable 여러개의 데이터를 페이지네이션 처리하기 위한 정보
     */
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersByUser(UserEntity user, Pageable pageable) {

        return orderRepository.findOrderByUser(user, pageable)
                .map(OrderDto::from);
    }

    /**
     * 가게의 주문내역을 조회한다.
     *
     * @param shopId 가게의 주문을 확인하기 위한 정보
     * @param user 가게의 주인이 맞는지 확인하기 위한 정보
     * @param pageable 여러개의 데이터를 페이지네이션 처리하기 위한 정보
     */
    @Transactional(readOnly = true)
    public Page<ShopOrderDto> getOrderByShopId(UUID shopId, UserEntity user, Pageable pageable) {
        ShopEntity shop = shopService.getShopById(shopId);
        shopService.validateShopOwner(shop.getId(), user.getId());

        return orderRepository.findOrderByShopId(shopId, pageable)
                .map(ShopOrderDto::from);
    }

    /**
     * 주문 내역 삭제 (soft delete)
     *
     * @param orderId 삭제할 주문 PK
     * @param user 본인의 주문인지 확인하기 위한 정보
     */
    public void delete(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        order.validateCustomer(user.getId());

        order.delete(user.getId());
    }

    // ========== 주문 상태 변경 (OWNER) ==========

    /**
     * 주문 수락 (사장님)
     *
     * @param orderId 주문 ID
     * @param user 사장님 정보
     */
    public void acceptOrder(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        shopService.validateShopOwner(order.getShopId(), user.getId());
        order.accept();
    }

    /**
     * 주문 거절 (사장님)
     *
     * @param orderId 주문 ID
     * @param user 사장님 정보
     */
    public void rejectOrder(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        shopService.validateShopOwner(order.getShopId(), user.getId());
        paymentServiceV1.cancelPayment(user.getId(), orderId, "사장 주문 거절");
        order.reject();
    }

    /**
     * 조리 시작 (사장님)
     *
     * @param orderId 주문 ID
     * @param user 사장님 정보
     */
    public void startCooking(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        shopService.validateShopOwner(order.getShopId(), user.getId());
        order.startCooking();
    }

    /**
     * 조리 완료 (사장님)
     *
     * @param orderId 주문 ID
     * @param user 사장님 정보
     */
    public void completeCooking(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        shopService.validateShopOwner(order.getShopId(), user.getId());
        order.completeCooking();
    }

    /**
     * 배달 시작 (사장님)
     *
     * @param orderId 주문 ID
     * @param user 사장님 정보
     */
    public void startDelivery(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        shopService.validateShopOwner(order.getShopId(), user.getId());
        order.startDelivery();
    }

    /**
     * 배달 완료 (사장님)
     *
     * @param orderId 주문 ID
     * @param user 사장님 정보
     */
    public void completeDelivery(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        shopService.validateShopOwner(order.getShopId(), user.getId());
        order.completeDelivery();
    }

    // ========== 주문 상태 변경 (CUSTOMER) ==========

    /**
     * 주문 취소 (고객)
     *
     * @param orderId 주문 ID
     * @param user 고객 정보
     */
    public void cancelOrder(UUID orderId, UserEntity user) {
        OrderEntity order = getById(orderId);
        order.validateCustomer(user.getId());
        paymentServiceV1.cancelPayment(user.getId(), orderId, "고객 주문 취소");
        order.cancel();
    }

    // ========== private 헬퍼 메서드 ==========

    /**
     * 메뉴 ID를 키로 갖는 메뉴 엔티티 Map 반환
     *
     * @param menuInfoList 주문 요청에 포함된 메뉴 정보
     * @return 메뉴 ID를 키로 갖는 메뉴 엔티티 Map 반환
     * @throws IllegalArgumentException 존재하지 않는 메뉴 ID가 포함된 경우
     */
    private Map<UUID, MenuEntity> getMenuMap(List<CreateOrderRequest.MenuInfo> menuInfoList) {
        List<UUID> menuIdList = menuInfoList.stream()
                .map(CreateOrderRequest.MenuInfo::getMenuId)
                .toList();

        List<MenuEntity> menuList = menuService.getMenusByIds(menuIdList);

        if (menuList.size() != menuInfoList.size()) {
            throw new MenusNotFoundInOrderException(MENU_NOT_FOUNT_IN_ORDER);
        }

        return menuList.stream()
                .collect(Collectors.toMap(
                        MenuEntity::getId,
                        Function.identity())
                );
    }

    /**
     * 주문 요청 정보를 기반으로 OrderMenu 엔티티 리스트 생성
     *
     * @param menuInfoList 주문 요청의 메뉴 정보 (ID, 수량)
     * @param menuMap 메뉴 ID를 키로 하는 메뉴 엔티티 맵
     * @param orderEntity 생성된 주문 엔티티
     * @return 생성된 주문 메뉴 엔티티 리스트
     */
    private List<OrderMenuEntity> createOrderMenusFromRequest(
            List<CreateOrderRequest.MenuInfo> menuInfoList,
            Map<UUID, MenuEntity> menuMap,
            OrderEntity orderEntity
    ) {
        return menuInfoList.stream()
                .map(menuInfo -> {
                    MenuEntity menu = menuMap.get(menuInfo.getMenuId());

                    return OrderMenuEntity.create(
                            orderEntity,
                            menu,
                            menuInfo
                    );
                }).toList();
    }

    /**
     * 주문 조회 에러 처리
     *
     * @param orderId 조회할 주문의 PK
     */
    private OrderEntity getById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ORDER_NOT_FOUND)
        );
    }
}