package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "주문 목록 응답")
@Getter
@Builder
@RequiredArgsConstructor
public class OrderResponse {

    @Schema(description = "주문 ID")
    private final UUID orderId;

    @Schema(description = "가게 ID")
    private final UUID shopId;

    @Schema(description = "가게 이름")
    private final String shopName;

    @Schema(description = "총 금액")
    private final int totalPrice;

    @Schema(description = "주문 일시")
    private final LocalDateTime orderDate;

    @Schema(description = "주문 메뉴 목록")
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

        public static MenuInfo from(OrderMenuInfo info) {
            return MenuInfo.builder()
                    .menuName(info.getMenuName())
                    .quantity(info.getQuantity())
                    .price(info.getPrice())
                    .build();
        }
    }
}