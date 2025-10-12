package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import com.sparta.bapzip.order.application.dto.ShopOrderDto;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopOrderResponse {
    private final UUID orderId;
    private final String customerName;
    private final OrderStatus orderStatus;
    private final int totalPrice;
    private final String deliveryAddress;
    private final LocalDateTime orderDate;
    private final List<MenuInfo> menuInfoList;

    public static ShopOrderResponse from(ShopOrderDto dto) {
        return ShopOrderResponse.builder()
                .orderId(dto.getOrderId())
                .customerName(dto.getCustomerName())
                .orderStatus(dto.getOrderStatus())
                .totalPrice(dto.getTotalPrice())
                .deliveryAddress(dto.getDeliveryAddress())
                .orderDate(dto.getOrderDate())
                .menuInfoList(dto.getMenuInfoList().stream()
                        .map(ShopOrderResponse.MenuInfo::from)
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

        public static ShopOrderResponse.MenuInfo from(OrderMenuInfo info) {
            return ShopOrderResponse.MenuInfo.builder()
                    .menuName(info.getMenuName())
                    .quantity(info.getQuantity())
                    .build();
        }
    }
}
