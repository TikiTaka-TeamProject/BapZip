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
@Schema(description = "결제 취소 요청 등록 응답형식")
public class PaymentCancelRequest {

    @Schema(description = "", example = "")
    @NotNull(message = "주문 아이디는 필수입니다.")
    private UUID orderId;
    @Schema(description = "", example = "")
    @NotNull(message = "결제 토큰(토스에서 발급해준 고유 키) 필수입니다.")
    private String paymentKey;
    @Schema(description = "", example = "")
    private LocalDateTime requestedAt;
    @Schema(description = "", example = "")
    private LocalDateTime approvedAt;
    @NotNull(message = "취소 사유는 필수입니다.")
    @Schema(description = "", example = "")
    private String cancelReason;
}
