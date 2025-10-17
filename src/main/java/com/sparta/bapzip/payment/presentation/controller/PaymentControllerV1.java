package com.sparta.bapzip.payment.presentation.controller;
import com.sparta.bapzip.payment.application.PaymentServiceV1;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentCancelRequest;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentCreateRequest;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import com.sparta.bapzip.servicearea.presentation.dto.response.AreaSaveResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
@Tag(name = "결제", description = "결제 api")
public class PaymentControllerV1 {
    private final PaymentServiceV1 paymentService;
    @Operation(summary = "결제 승인 요청 등록", description = "결제 승인 요청 등록 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "결제 승인 요청 등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = PaymentResponseDto.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 200,
                                                       "data": {
                                                           "paymentKey": "tviva20251016154506lYT22",
                                                           "orderId": "e98daf86-bb2e-48b4-b217-050107a705a2",
                                                           "status": "SUCCESS",
                                                           "totalPrice": 12000,
                                                           "approvedAt": "2025-10-16T15:45:06",
                                                           "canceledAt": null
                                                       }
                                                     }
                                                    """
                                    )
                            }
                    )

            )
    })
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponseDto> createPayment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PaymentCreateRequest requestDto) {
        PaymentResponseDto response = paymentService.createPayment(requestDto);

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "결제 취소 요청 등록", description = "결제 취소 요청 등록 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "결제 취소 요청 등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = PaymentCancelRequest.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 200,
                                                       "data": {
                                                            "paymentKey": "tviva20251016154506lYT22",
                                                            "orderId": "e98daf86-bb2e-48b4-b217-050107a705a2",
                                                            "status": "CANCELED",
                                                            "totalPrice": 12000,
                                                            "approvedAt": "2025-10-16T15:45:06",
                                                            "canceledAt": "2025-10-16T15:50:06"
                                                        }
                                                     }
                                                    """
                                    )
                            }
                    )

            )
    })
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<PaymentResponseDto> cancelPayment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String orderId, @RequestBody String cancelReason) {
       PaymentResponseDto response = paymentService.cancelPayment(userDetails.getUser().getId(), UUID.fromString(orderId), cancelReason);
        return ResponseEntity.ok(response);
    }
}