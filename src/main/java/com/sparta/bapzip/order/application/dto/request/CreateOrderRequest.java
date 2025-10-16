package com.sparta.bapzip.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Schema(description = "주문 생성 요청")
@Getter
@Builder
public class CreateOrderRequest {

    @Schema(description = "가게 ID", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "가게 정보(가게 ID)가 포함되어 있지 않습니다.")
    private UUID shopId;

    @Schema(description = "배달 주소", example = "서울시 강남구 테헤란로 123")
    @NotBlank(message = "배달 주소가 포함되어 있지 않습니다.")
    private String deliveryAddress;

    @Schema(description = "상세 주소", example = "101동 202호")
    @NotBlank(message = "배달 상세 주소가 포함되어 있지 않습니다.")
    private String detailAddress;

    @Schema(description = "고객 전화번호", example = "010-1234-5678")
    @NotBlank(message = "고객님의 전화번호가 포함되어 있지 않습니다.")
    private String customerPhone;

    @Schema(description = "주문 메뉴 목록")
    @NotNull(message = "메뉴가 1개 이상 포함되어 있어야 합니다.")
    private List<MenuInfo> menuInfoList;

    @Schema(description = "결제 방식", example = "CARD")
    @NotBlank(message = "결제 방식이 포함되어 있지 않습니다.")
    private String paymentType;

    @Schema(description = "요청 사항", example = "문 앞에 놓아주세요")
    private String specialRequest;

    @Schema(description = "메뉴 정보")
    @Getter
    @AllArgsConstructor
    public static class MenuInfo {
        @Schema(description = "메뉴 ID", example = "550e8400-e29b-41d4-a716-446655440002")
        @NotNull(message = "메뉴 정보(메뉴 ID)가 포함되어 있지 않습니다.")
        private UUID menuId;

        @Schema(description = "주문 수량", example = "2")
        @NotNull(message = "1개 이상 주문할 수 있습니다.")
        private int quantity;
    }
}