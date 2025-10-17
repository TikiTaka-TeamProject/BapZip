package com.sparta.bapzip.payment.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "결제 취소 요청 등록형식")
public class PaymentCancelRequest {

    @Schema(description = "주문 아이디", example = "e98daf86-bb2e-48b4-b217-050107a705a2")
    private UUID orderId;

    @Schema(description = "Toss 결제 키", example = "tviva20251014125727VUdJ5")
    private String paymentKey;

    @NotNull(message = "취소 사유는 필수입니다.")
    @Schema(description = "취소 사유", example = "주문자 변심")
    private String cancelReason;
}
