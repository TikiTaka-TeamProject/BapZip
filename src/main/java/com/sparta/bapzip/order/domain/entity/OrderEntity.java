package com.sparta.bapzip.order.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.order.domain.exception.MenuNotInShopException;
import com.sparta.bapzip.order.domain.exception.SoldOutMenuException;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String paymentType;

    @Column(nullable = false)
    private int totalPrice;

    private String specialRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderMenuEntity> orderMenuList = new ArrayList<>();

    // 비즈니스 로직

    /**
     * 주문 생성
     */
    public static OrderEntity create(
            CreateOrderRequest request,
            UUID shopId,
            Map<UUID, MenuEntity> menuMap
    ) {
        validateManusInShop(menuMap, shopId);
        validateNoSoldOutMenus(menuMap);

        int totalPrice = calculateMenuTotalPrice(request.getMenuInfoList(), menuMap);

        return OrderEntity.builder()
                .status(OrderStatus.PENDING)
                .deliveryAddress(request.getDeliveryAddress())
                .detailAddress(request.getDetailAddress())
                .paymentType(request.getPaymentType())
                .totalPrice(totalPrice)
                .specialRequests(request.getSpecialRequest())
                .build();
    }

    /**
     * 메뉴가 해당 가게의 메뉴인지 검증
     *
     * @param menuMap 메뉴 ID를 키로 갖는 메뉴 엔티티 Map
     * @param shopId 주문하려는 가게의 ID
     * @throws MenuNotInShopException 다른 가게의 메뉴가 포함된 경우
     */
    private static void validateManusInShop(Map<UUID, MenuEntity> menuMap, UUID shopId) {
        boolean allMenusFromShop = menuMap.values().stream()
                .allMatch(menu -> menu.getShop().getId().equals(shopId));

        if (!allMenusFromShop) {
            throw new MenuNotInShopException(ErrorCode.MENU_NOT_IN_SHOP);
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
            throw new SoldOutMenuException(ErrorCode.SOLD_OUT_MENU);
        }
    }

    // 연관관계 설정 로직

    /**
     * 자식 엔티티 orderMenu 설정
     */
    public void addOrderMenu(OrderMenuEntity orderMenu) {
        orderMenuList.add(orderMenu);
        orderMenu.addOrder(this);
    }
}
