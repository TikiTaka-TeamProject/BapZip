package com.sparta.bapzip.payment.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymentCreateRequest {

    private int amount;
    private String orderId;
    private String orderName;

    private String cardNumber;
    private String cardExpirationYear;
    private String cardExpirationMonth;
    private String cardPassword;
    private String customerIdentityNumber;

    private String paymentKey;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "amount=" + amount +
                ", orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", requestedAt=" + requestedAt +
                ", approvedAt=" + approvedAt +
                ", paymentKey='" + paymentKey + '\'' +
                '}';
    }
}
