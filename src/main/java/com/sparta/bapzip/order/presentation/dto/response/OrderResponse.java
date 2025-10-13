package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
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
    private final UUID shopId;
    private final String shopName;
    private final int totalPrice;
    private final LocalDateTime orderDate;
    private final List<MenuInfo> menuInfoList;

    public static OrderResponse from(OrderDto dto) {
        return OrderResponse.builder()
                .orderId(dto.getOrderId())
                .shopId(dto.getShopId())
                .shopName(dto.getShopName())
                .totalPrice(dto.getTotalPrice())
                .orderDate(dto.getOrderDate())
                .menuInfoList(dto.getMenuInfoList().stream()
                        .map(MenuInfo::from)
                        .toList())
                .build();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class MenuInfo {
        private final String menuName;
        private final int quantity;
        private final int price;

        public static MenuInfo from(OrderMenuInfo info) {
            return MenuInfo.builder()
                    .menuName(info.getMenuName())
                    .quantity(info.getQuantity())
                    .price(info.getPrice())
                    .build();
        }
    }
}
