package com.sparta.bapzip.payment.presentation.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PaymentResponseDto {

    private String paymentKey;      // Toss 결제 키
    private String orderId;
    private String status;          // 결제 상태 SUCCESS, FAIL
    private int totalPrice;        // 결제 금액
    private LocalDateTime approvedAt; // API 결제 요청 응답 시각
    private LocalDateTime canceledAt; // API 취소 요청 응답 시각

}
