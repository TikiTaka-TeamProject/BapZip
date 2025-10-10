package com.sparta.bapzip.payment.presentation.controller;

import com.sparta.bapzip.payment.application.PaymentServiceV1;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentCreateRequest;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentCreateRequest requestDto) {
        PaymentResponseDto response = paymentService.createPaymentWithCard(UUID.randomUUID(), requestDto);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<PaymentResponseDto> createPayment(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable String orderId, @RequestBody String cancelReason) {

        PaymentResponseDto response = paymentService.cancelPayment(user.getUser().getId(), UUID.fromString(orderId), cancelReason);
        return ResponseEntity.ok(response);
    }
}