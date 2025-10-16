package com.sparta.bapzip.payment.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString

@Schema(description = "결제 승인/취소 요청 등록 응답형식")
public class PaymentResponseDto {
    @Schema(description = "Toss 결제 키", example = "tviva20251014125727VUdJ5")
    private String paymentKey;      // Toss 결제 키
    @Schema(description = "주문 번호 식별자", example = "7294b430-57f8-467b-b6c1-09a3ceae3186")
    private String orderId;
    @Schema(description = "API 응답 상태", example = "7294b430-57f8-467b-b6c1-09a3ceae3186")
    private String status;          // 결제 상태 SUCCESS, FAIL
    @Schema(description = "결제 금액", example = "12000")
    private int totalPrice;        // 결제 금액
    @Schema(description = "API 요청 응답 시각", example = "2025-10-14 12:57:26.649000")
    private LocalDateTime approvedAt; // API 결제 요청 응답 시각
    @Schema(description = "API 취소 요청 응답 시각", example = "2025-10-14 12:57:26.649000")
    private LocalDateTime canceledAt; // API 취소 요청 응답 시각

}
