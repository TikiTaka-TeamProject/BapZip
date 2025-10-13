package com.sparta.bapzip.order.application.dto;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderDetailDto {
    private final UUID orderId;
    private final UUID shopId;
    private final String shopName;
    private final Long customerId;
    private final String customerName;
    private final OrderStatus orderStatus;
    private final List<OrderMenuInfo> menuInfoList;
    private final int totalPrice;
    private final String deliveryAddress;
    private final String paymentType;
    private final String specialRequests;
    private final LocalDateTime orderDate;

    public static OrderDetailDto from(OrderEntity order) {
        return OrderDetailDto.builder()
                .orderId(order.getId())
                .shopId(order.getShopId())
                .shopName(order.getShopName())
                .customerId(order.getUser().getId())
                .customerName(order.getUser().getName())
                .orderStatus(order.getStatus())
                .menuInfoList(order.getOrderMenuList().stream()
                        .map(OrderMenuInfo::from)
                        .toList())
                .totalPrice(order.getTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .paymentType(order.getPaymentType())
                .specialRequests(order.getSpecialRequests())
                .orderDate(order.getCreatedAt())
                .build();
    }
}
