package com.sparta.bapzip.order.application.dto;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderDto {
    private final UUID orderId;
    private final UUID storeId;
    private final String shopName;
    private final int totalPrice;
    private final LocalDateTime orderDate;
    private final List<MenuInfo> menuInfoList;

    public static OrderDto from(OrderEntity order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .storeId(order.getShopId())
                .shopName(order.getShopName())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getCreatedAt())
                .menuInfoList(order.getOrderMenuList().stream()
                        .map(MenuInfo::from)
                        .toList())
                .build();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class MenuInfo {
        private final String name;
        private final int quantity;
        private final int price;

        public static MenuInfo from(OrderMenuEntity orderMenu) {
            return MenuInfo.builder()
                    .name(orderMenu.getName())
                    .quantity(orderMenu.getQuantity())
                    .price(orderMenu.getPrice())
                    .build();
        }
    }
}
