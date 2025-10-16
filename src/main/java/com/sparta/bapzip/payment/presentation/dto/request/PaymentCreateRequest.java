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
    @Schema(description = "", example = "")
    @NotNull(message = "결제 진행할 주문의 아이디는 필수입니다.")
    private UUID orderId;
    @Schema(description = "", example = "")
    @NotNull
    private String orderName;
    @Schema(description = "", example = "")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardNumber;
    @Schema(description = "", example = "")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardExpirationYear;
    @Schema(description = "", example = "")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardExpirationMonth;
    @Schema(description = "", example = "")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String cardPassword;
    @Schema(description = "", example = "")
    @NotNull(message = "카드 정보는 필수입니다.")
    private String customerIdentityNumber; // 카드 소유자 생년월일 6자리=>YYMMDD 형식, 토스에서 요구함

    @Schema(description = "", example = "")
    private LocalDateTime requestedAt;

    @Schema(description = "", example = "")
    private LocalDateTime approvedAt;

}
