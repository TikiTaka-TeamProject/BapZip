package com.sparta.bapzip.payment.presentation.controller;

import com.sparta.bapzip.payment.application.PaymentServiceV1;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentRequest;
import com.sparta.bapzip.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequest requestDto) {
        PaymentResponseDto response = paymentService.createPaymentWithCard(UUID.randomUUID(), requestDto);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<PaymentResponseDto> createPayment(@PathVariable String orderId, @RequestBody String cancelReason) {

        PaymentResponseDto response = paymentService.cancelPayment(UUID.fromString(orderId), cancelReason);
        return ResponseEntity.ok(response);
    }
}