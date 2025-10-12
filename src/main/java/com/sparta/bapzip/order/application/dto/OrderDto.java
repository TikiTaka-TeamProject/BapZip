package com.sparta.bapzip.order.application.dto;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
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
    private final UUID shopId;
    private final String shopName;
    private final int totalPrice;
    private final LocalDateTime orderDate;
    private final List<OrderMenuInfo> menuInfoList;

    public static OrderDto from(OrderEntity order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .shopId(order.getShopId())
                .shopName(order.getShopName())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getCreatedAt())
                .menuInfoList(order.getOrderMenuList().stream()
                        .map(OrderMenuInfo::from)
                        .toList())
                .build();
    }
}
