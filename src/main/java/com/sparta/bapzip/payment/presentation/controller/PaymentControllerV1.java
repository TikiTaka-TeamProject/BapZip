package com.sparta.bapzip.payment.presentation.controller;
import com.sparta.bapzip.payment.application.PaymentServiceV1;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentCreateRequest;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
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
public class PaymentControllerV1 {
    private final PaymentServiceV1 paymentService;
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponseDto> createPayment(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody PaymentCreateRequest requestDto) {
        PaymentResponseDto response = paymentService.createPaymentWithCard(UUID.fromString("6a7b8230-b26b-45db-bcd0-84fb189ff53b"), requestDto);

        return ResponseEntity.ok(response);
    }
    // 로그인 기능 연결 이후 보완 필요
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<PaymentResponseDto> cancelPayment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String orderId, @RequestBody String cancelReasonJson) {
       PaymentResponseDto response = paymentService.cancelPayment(userDetails.getUser().getId(), UUID.fromString(orderId), cancelReasonJson);
        return ResponseEntity.ok(response);
    }
}