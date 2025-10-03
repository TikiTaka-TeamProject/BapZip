package com.sparta.bapzip.payment.presentation.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Getter
@Setter
public class PaymentResponseDto {

    private String paymentKey;      // Toss 결제 키
    private String orderId;
    private String status;          // 결제 상태 SUCCESS, FAIL
    private int totalPrice;        // 결제 금액
    private LocalDateTime approvedAt; // 결제 승인 시각
    private LocalDateTime requestedAt;  // 결제 요청 시각

}
