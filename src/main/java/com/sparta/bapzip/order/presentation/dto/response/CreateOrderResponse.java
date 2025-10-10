package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderCreationResult;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

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

    public static CreateOrderResponse from(OrderCreationResult result) {

        List<MenuInfo> mappedMenuInfoList = result.getMenuInfoList().stream()
                .map(MenuInfo::from)
                .toList();

        String formattedCreatedAt = result.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return CreateOrderResponse.builder()
                .orderId(result.getOrderId())
                .shopId(result.getShopId())
                .userId(result.getUserId())
                .shopName(result.getShopName())
                .status(result.getStatus())
                .menuInfoList(mappedMenuInfoList)
                .totalAmount(result.getTotalAmount())
                .deliveryAddress(result.getDeliveryAddress())
                .detailAddress(result.getDetailAddress())
                .paymentType(result.getPaymentType())
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
