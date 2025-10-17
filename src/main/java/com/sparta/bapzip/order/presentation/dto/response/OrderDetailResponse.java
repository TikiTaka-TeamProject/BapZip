package com.sparta.bapzip.order.presentation.dto.response;

import com.sparta.bapzip.order.application.dto.OrderDetailDto;
import com.sparta.bapzip.order.application.dto.OrderMenuInfo;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "주문 상세 응답")
@Getter
@Builder
public class OrderDetailResponse {
    @Schema(description = "주문 ID")
    private final UUID orderId;

    @Schema(description = "가게 ID")
    private final UUID shopId;

    @Schema(description = "가게 이름")
    private final String shopName;

    @Schema(description = "주문자 ID")
    private final Long customerId;

    @Schema(description = "주문자 이름")
    private final String customerName;

    @Schema(description = "주문 상태")
    private final OrderStatus orderStatus;

    @Schema(description = "주문 메뉴 목록")
    private final List<MenuInfo> menuInfoList;

    @Schema(description = "총 금액")
    private final int totalPrice;

    @Schema(description = "배달 주소")
    private final String deliveryAddress;

    @Schema(description = "결제 방식")
    private final String paymentType;

    @Schema(description = "요청 사항")
    private final String specialRequests;

    @Schema(description = "주문 일시")
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

    @Schema(description = "메뉴 정보")
    @Getter
    @Builder
    public static class MenuInfo {
        @Schema(description = "메뉴 ID")
        private final UUID menuId;

        @Schema(description = "메뉴 이름")
        private final String menuName;

        @Schema(description = "메뉴 가격")
        private final int price;

        @Schema(description = "주문 수량")
        private final int quantity;

        @Schema(description = "소계")
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
