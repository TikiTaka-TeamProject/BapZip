package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderCreationResult;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.*;

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
    private final String createdAt;

    public static CreateOrderResponse from(OrderCreationResult serviceResponse) {

        List<MenuInfo> mappedMenuInfoList = serviceResponse.getMenuInfoList().stream()
                .map(MenuInfo::from)
                .toList();

        String formattedCreatedAt = serviceResponse.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return CreateOrderResponse.builder()
                .orderId(serviceResponse.getOrderId())
                .shopId(serviceResponse.getShopId())
                .userId(serviceResponse.getUserId())
                .shopName(serviceResponse.getShopName())
                .status(serviceResponse.getStatus())
                .menuInfoList(mappedMenuInfoList)
                .totalAmount(serviceResponse.getTotalAmount())
                .deliveryAddress(serviceResponse.getDeliveryAddress())
                .detailAddress(serviceResponse.getDetailAddress())
                .paymentType(serviceResponse.getPaymentType())
                .createdAt(formattedCreatedAt)
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

        public static MenuInfo from(OrderCreationResult.MenuInfo serviceMenuInfo) {
            return MenuInfo.builder()
                    .menuId(serviceMenuInfo.getMenuId())
                    .menuName(serviceMenuInfo.getMenuName())
                    .price(serviceMenuInfo.getPrice())
                    .quantity(serviceMenuInfo.getQuantity())
                    .subtotal(serviceMenuInfo.getSubtotal())
                    .build();
        }
    }
}