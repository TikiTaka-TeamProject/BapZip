package com.sparta.bapzip.payment.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {

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
