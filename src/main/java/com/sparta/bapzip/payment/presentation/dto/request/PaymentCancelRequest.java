package com.sparta.bapzip.payment.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelRequest {
    @NotNull(message = "주문 아이디는 필수입니다.")
    private UUID orderId;
    @NotNull(message = "결제 토큰(토스에서 발급해준 고유 키) 필수입니다.")
    private String paymentKey;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    @NotNull(message = "취소 사유는 필수입니다.")
    private String cancelReason;

    @Override
    public String toString() {
        return "PaymsentCancelRequest{" +
                "orderId='" + orderId + '\'' +
                ", paymentKey='" + paymentKey + '\'' +
                ", requestedAt=" + requestedAt +
                ", approvedAt=" + approvedAt +
                ", cancelReason='" + cancelReason + '\'' +
                '}';
    }
}
