package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderDetailDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderDetailResponse {
    private final UUID orderId;
    private final UUID shopId;
    private final String shopName;
    private final Long customerId;
    private final String customerName;
    private final OrderStatus orderStatus;
    private final List<MenuInfo> menuInfoList;
    private final int totalPrice;
    private final String deliveryAddress;
    private final String paymentType;
    private final String specialRequests;
    private final LocalDateTime orderDate;

    public static OrderDetailResponse from(OrderDetailDto dto) {
        List<MenuInfo> mappedMenuInfoList = dto.getMenuInfoList().stream()
                .map(MenuInfo::from)
                .toList();

        return OrderDetailResponse.builder()
                .orderId(dto.getOrderId())
                .shopId(dto.getShopId())
                .shopName(dto.getShopName())
                .customerId(dto.getCustomerId())
                .customerName(dto.getCustomerName())
                .orderStatus(dto.getOrderStatus())
                .menuInfoList(mappedMenuInfoList)
                .totalPrice(dto.getTotalPrice())
                .deliveryAddress(dto.getDeliveryAddress())
                .paymentType(dto.getPaymentType())
                .specialRequests(dto.getSpecialRequests())
                .orderDate(dto.getOrderDate())
                .build();
    }

    @Getter
    @Builder
    public static class MenuInfo {
        private final UUID menuId;
        private final String menuName;
        private final int price;
        private final int quantity;
        private final int subtotal;

        public static MenuInfo from(OrderMenuInfo info) {
            return MenuInfo.builder()
                    .menuId(info.getMenuId())
                    .menuName(info.getMenuName())
                    .price(info.getPrice())
                    .quantity(info.getQuantity())
                    .subtotal(info.getSubtotal())
                    .build();
        }
    }
}