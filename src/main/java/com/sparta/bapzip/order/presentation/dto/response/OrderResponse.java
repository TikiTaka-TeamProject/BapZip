package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderResponse {
    private final UUID orderId;
    private final UUID storeId;
    private final String shopName;
    private final int totalPrice;
    private final LocalDateTime orderDate;
    private final List<OrderResponse.MenuInfo> menuInfoList;

    public static OrderResponse from(OrderDto orderDto) {
        return OrderResponse.builder()
                .orderId(orderDto.getOrderId())
                .storeId(orderDto.getStoreId())
                .shopName(orderDto.getShopName())
                .totalPrice(orderDto.getTotalPrice())
                .orderDate(orderDto.getOrderDate())
                .menuInfoList(orderDto.getMenuInfoList().stream()
                        .map(MenuInfo::from)
                        .toList())
                .build();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class MenuInfo {
        private final String name;
        private final int quantity;
        private final int price;

        public static MenuInfo from(OrderDto.MenuInfo menuInfo) {
            return MenuInfo.builder()
                    .name(menuInfo.getName())
                    .quantity(menuInfo.getQuantity())
                    .price(menuInfo.getPrice())
                    .build();
        }
    }
}
