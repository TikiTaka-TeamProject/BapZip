package com.sparta.bapzip.order.application.dto;


import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderCreationResult {

    private final UUID orderId;
    private final UUID shopId;
    private final Long userId;
    private final String shopName;
    private final OrderStatus status;
    private final List<MenuInfo> menuInfoList;
    private final int totalAmount;
    private final String deliveryAddress;
    private final String detailAddress;
    private final String paymentType;
    private final LocalDateTime createdAt;

    @Getter
    @Builder
    public static class MenuInfo {
        private final UUID menuId;
        private final String menuName;
        private final int price;
        private final int quantity;
        private final int subtotal;

        public static MenuInfo from(OrderMenuEntity entity) {
            return MenuInfo.builder()
                    .menuId(entity.getMenu().getId())
                    .menuName(entity.getName())
                    .price(entity.getPrice())
                    .quantity(entity.getQuantity())
                    .subtotal(entity.getSubtotal())
                    .build();
        }
    }

    public static OrderCreationResult from(
            OrderEntity orderEntity,
            List<OrderMenuEntity> orderMenuEntities,
            ShopEntity shopEntity,
            Long userId
    ) {
        List<MenuInfo> menuInfoList = orderMenuEntities.stream()
                .map(MenuInfo::from)
                .toList();

        return OrderCreationResult.builder()
                .orderId(orderEntity.getId())
                .shopId(shopEntity.getId())
                .userId(userId)
                .shopName(shopEntity.getName())
                .status(orderEntity.getStatus())
                .menuInfoList(menuInfoList)
                .totalAmount(orderEntity.getTotalPrice())
                .deliveryAddress(orderEntity.getDeliveryAddress())
                .detailAddress(orderEntity.getDetailAddress())
                .paymentType(orderEntity.getPaymentType())
                .createdAt(orderEntity.getCreatedAt())
                .build();
    }
}