package com.sparta.bapzip.payment.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "결제 승인 요청 등록 응답형식")
public class PaymentCreateRequest {
    @Schema(description = "결제 금액", example = "10000")
    private int amount;

    @Schema(description = "주문 아이디", example = "571b3db9-c2da-497d-ad1d-f9b1c732d75c")
    @NotNull(message = "결제 진행할 주문의 아이디는 필수입니다.")
    private UUID orderId;

    @Schema(description = "주문명", example = "홍콩 반점의 짜장면 외 1건")
    private String orderName;

    @Schema(description = "카드번호", example = "4111111111111111")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardNumber;

    @Schema(description = "카드 유효 연도", example = "28")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardExpirationYear;

    @Schema(description = "카드 유효 월", example = "12")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardExpirationMonth;

    @Schema(description = "비밀번호", example = "01")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardPassword;

    @Schema(description = "카드 소유자 생년월일 6자리", example = "860824")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String customerIdentityNumber; // 카드 소유자 생년월일 6자리=>YYMMDD 형식, 토스에서 요구함
}
