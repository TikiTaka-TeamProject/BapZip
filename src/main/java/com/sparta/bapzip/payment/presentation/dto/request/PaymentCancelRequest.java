package com.sparta.bapzip.payment.presentation.dto.request;

import java.time.LocalDateTime;
import lombok.*;

//@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelRequest {

    private String orderId;
    private String paymentKey;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
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
