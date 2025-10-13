package com.sparta.bapzip.order.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.domain.exception.ForbiddenOrderAccessException;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.order.domain.exception.*;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sparta.bapzip.global.exception.ErrorCode.*;
import static com.sparta.bapzip.order.domain.enums.OrderStatus.*;

@Entity
@Table(name = "p_orders")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID shopId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = PENDING;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String paymentType;

    @Column(nullable = false)
    private int totalPrice;

    private String specialRequests;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    @BatchSize(size = 100)
    @Builder.Default
    private List<OrderMenuEntity> orderMenuList = new ArrayList<>();

    // ========== 비즈니스 로직 ==========

    /**
     * 주문 생성
     */
    public static OrderEntity create(
            CreateOrderRequest request,
            UserEntity user,
            ShopEntity shop,
            Map<UUID, MenuEntity> menuMap
    ) {
        validateManusInShop(menuMap, shop);
        validateNoSoldOutMenus(menuMap);

        int totalPrice = calculateMenuTotalPrice(request.getMenuInfoList(), menuMap);

        return OrderEntity.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .deliveryAddress(request.getDeliveryAddress())
                .detailAddress(request.getDetailAddress())
                .paymentType(request.getPaymentType())
                .totalPrice(totalPrice)
                .specialRequests(request.getSpecialRequest())
                .user(user)
                .build();
    }

    /**
     * 메뉴가 해당 가게의 메뉴인지 검증
     *
     * @param menuMap 메뉴 ID를 키로 갖는 메뉴 엔티티 Map
     * @param shop 주문하려는 가게
     * @throws MenuNotInShopException 다른 가게의 메뉴가 포함된 경우
     */
    private static void validateManusInShop(Map<UUID, MenuEntity> menuMap, ShopEntity shop) {
        boolean allMenusFromShop = menuMap.values().stream()
                .allMatch(menu -> menu.getShop().getId().equals(shop.getId()));

        if (!allMenusFromShop) {
            throw new MenuNotInShopException(MENU_NOT_IN_SHOP);
        }
    }

    /**
     * 주문 요청에 포함된 모든 메뉴 가격의 총합
     *
     * @param menuInfoList 주문 요청에서 전달된 메뉴 ID와 수량 정보 리스트
     * @return 주문 총 금액 (int)
     * @throws IllegalArgumentException 존재하지 않는 메뉴 ID가 포함된 경우
     */
    private static int calculateMenuTotalPrice(
            List<CreateOrderRequest.MenuInfo> menuInfoList,
            Map<UUID, MenuEntity> menuMap
    ) {
        return menuInfoList.stream()
                .mapToInt(menuInfo -> {
                    MenuEntity menu = menuMap.get(menuInfo.getMenuId());
                    return menu.getPrice() * menuInfo.getQuantity();
                }).sum();
    }

    /**
     * 주문 요청에 포함된 메뉴 중 품절(SOLD_OUT) 상태의 메뉴가 있는지 검증
     *
     * @param menuMap 메뉴 ID를 키로 갖는 메뉴 엔티티 Map
     * @throws SoldOutMenuException 품절된 메뉴가 포함된 경우
     */
    private static void validateNoSoldOutMenus(Map<UUID, MenuEntity> menuMap) {
        boolean hasSoldOutMenu = menuMap.values().stream()
                .anyMatch(MenuEntity::isSoldOut);

        if (hasSoldOutMenu) {
            throw new SoldOutMenuException(SOLD_OUT_MENU);
        }
    }

    /**
     * 고객 권한 검증
     *
     * @param customerId 검증할 고객 ID
     * @throws ForbiddenOrderAccessException 권한이 없는 경우
     */
    public void validateCustomer(Long customerId) {
        if (!this.user.getId().equals(customerId)) {
            throw new ForbiddenOrderAccessException(FORBIDDEN_ORDER_ACCESS);
        }
    }

    // ========== 상태 변경 비즈니스 로직 ==========

    /**
     * 주문 수락
     *
     * @throws OrderNotPendingException 대기 중인 주문이 아닌 경우
     */
    public void accept() {
        if (this.status != PENDING) {
            throw new OrderNotPendingException(ORDER_NOT_PENDING);
        }
        this.status = ACCEPTED;
    }

    /**
     * 주문 거절
     *
     * @throws OrderNotPendingException 대기 중인 주문이 아닌 경우
     */
    public void reject() {
        if (this.status != PENDING) {
            throw new OrderNotPendingException(ORDER_NOT_PENDING);
        }

        this.status = REJECTED;
    }

    /**
     * 조리 시작
     *
     * @throws OrderNotAcceptedException 수락된 주문이 아닌 경우
     */
    public void startCooking() {
        if (this.status != ACCEPTED) {
            throw new OrderNotAcceptedException(ORDER_NOT_ACCEPTED);
        }
        this.status = COOKING;
    }

    /**
     * 조리 완료
     *
     * @throws OrderNotCookingException 조리 중인 주문이 아닌 경우
     */
    public void completeCooking() {
        if (this.status != COOKING) {
            throw new OrderNotCookingException(ORDER_NOT_COOKING);
        }
        this.status = COOK_COMPLETED;
    }

    /**
     * 배달 시작
     *
     * @throws OrderNotCookCompletedException 조리 완료된 주문이 아닌 경우
     */
    public void startDelivery() {
        if (this.status != COOK_COMPLETED) {
            throw new OrderNotCookCompletedException(ORDER_NOT_COOK_COMPLETED);
        }
        this.status = DELIVERING;
    }

    /**
     * 배달 완료
     *
     * @throws OrderNotDeliveringException 배달 중인 주문이 아닌 경우
     */
    public void completeDelivery() {
        if (this.status != DELIVERING) {
            throw new OrderNotDeliveringException(ORDER_NOT_DELIVERING);
        }
        this.status = DELIVERED;
    }

    /**
     * 주문 취소
     *
     * @throws OrderNotCancellableException 취소 불가능한 상태인 경우
     */
    public void cancel() {
        if (this.status != PENDING) {
            throw new OrderNotCancellableException(ORDER_NOT_CANCELLABLE);
        }
        this.status = CANCELED;
    }

    // ========== 연관관계 설정 로직 ==========

    /**
     * 자식 엔티티 orderMenu 설정
     */
    public void addOrderMenu(OrderMenuEntity orderMenu) {
        orderMenuList.add(orderMenu);
        orderMenu.addOrder(this);
    }

}