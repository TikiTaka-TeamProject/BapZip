package com.sparta.bapzip.payment.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bapzip.payment.application.PaymentServiceV1;
import com.sparta.bapzip.payment.presentation.dto.request.PaymentCreateRequest;
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
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentCreateRequest requestDto) {
//        PaymentResponseDto response = paymentService.createPaymentWithCard(UUID.randomUUID(), requestDto);
        PaymentResponseDto response = paymentService.createPaymentWithCard(UUID.fromString("d8b07e2e-8bcb-431c-b906-ee2d1deb71d8"), requestDto);

        return ResponseEntity.ok(response);
    }
    // 로그인 기능 연결 이후 보완 필요
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<PaymentResponseDto> cancelPayment(@PathVariable String orderId, @RequestBody String cancelReasonJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(cancelReasonJson);
        String cancelReason = node.path("cancelReason").asText();
        PaymentResponseDto response = paymentService.cancelPayment(1l, UUID.fromString(orderId), cancelReason);
        return ResponseEntity.ok(response);
    }
}