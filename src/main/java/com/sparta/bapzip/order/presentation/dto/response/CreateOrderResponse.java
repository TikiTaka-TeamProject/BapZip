package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderCreationDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateOrderResponse {

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

    public static CreateOrderResponse from(OrderCreationDto dto) {

        List<MenuInfo> mappedMenuInfoList = dto.getMenuInfoList().stream()
                .map(MenuInfo::from)
                .toList();

        return CreateOrderResponse.builder()
                .orderId(dto.getOrderId())
                .shopId(dto.getShopId())
                .userId(dto.getUserId())
                .shopName(dto.getShopName())
                .status(dto.getStatus())
                .menuInfoList(mappedMenuInfoList)
                .totalAmount(dto.getTotalPrice())
                .deliveryAddress(dto.getDeliveryAddress())
                .detailAddress(dto.getDetailAddress())
                .paymentType(dto.getPaymentType())
                .createdAt(dto.getCreatedAt())
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
