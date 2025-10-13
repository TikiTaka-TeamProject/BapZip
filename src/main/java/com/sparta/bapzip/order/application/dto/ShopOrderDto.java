package com.sparta.bapzip.order.application.dto;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopOrderDto {
    private final UUID orderId;
    private final String customerName;
    private final OrderStatus orderStatus;
    private final int totalPrice;
    private final String deliveryAddress;
    private final LocalDateTime orderDate;
    private final List<OrderMenuInfo> menuInfoList;

    public static ShopOrderDto from(OrderEntity order) {
        return ShopOrderDto.builder()
                .orderId(order.getId())
                .customerName(order.getUser().getName())
                .orderStatus(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .orderDate(order.getCreatedAt())
                .menuInfoList(order.getOrderMenuList().stream()
                        .map(OrderMenuInfo::from)
                        .toList())
                .build();
    }
}
