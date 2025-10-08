package com.sparta.bapzip.payment.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymentCreateRequest {

    @NotNull(message = "결제 금액은 필수입니다.")
    private int amount;
    @NotNull(message = "결제 진행할 주문의 아이디는 필수입니다.")
    private String orderId;
    @NotNull
    private String orderName;
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardNumber;
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardExpirationYear;
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardExpirationMonth;
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardPassword;
    @NotNull(message = "카드 정보는 필수입니다.")
    private String customerIdentityNumber; // 카드 소유자 생년월일 6자리=>YYMMDD 형식, 토스에서 요구함

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
                '}';
    }
}
