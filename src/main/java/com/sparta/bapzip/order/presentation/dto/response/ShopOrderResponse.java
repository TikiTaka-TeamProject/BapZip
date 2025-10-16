package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.ShopOrderDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "가게 주문 응답")
@Getter
@Builder
@RequiredArgsConstructor
public class ShopOrderResponse {
    @Schema(description = "주문 ID")
    private final UUID orderId;

    @Schema(description = "주문자 이름")
    private final String customerName;

    @Schema(description = "주문 상태")
    private final OrderStatus orderStatus;

    @Schema(description = "총 금액")
    private final int totalPrice;

    @Schema(description = "배달 주소")
    private final String deliveryAddress;

    @Schema(description = "주문 일시")
    private final LocalDateTime orderDate;

    @Schema(description = "주문 메뉴 목록")
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

    @Schema(description = "메뉴 정보")
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class MenuInfo {
        @Schema(description = "메뉴 이름")
        private final String menuName;

        @Schema(description = "주문 수량")
        private final int quantity;

        @Schema(description = "메뉴 가격")
        private final int price;

        public static ShopOrderResponse.MenuInfo from(OrderMenuInfo info) {
            return ShopOrderResponse.MenuInfo.builder()
                    .menuName(info.getMenuName())
                    .quantity(info.getQuantity())
                    .price(info.getPrice())
                    .build();
        }
    }
}