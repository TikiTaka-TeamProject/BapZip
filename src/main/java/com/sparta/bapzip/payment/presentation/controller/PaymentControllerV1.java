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

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentControllerV1 {
    private final PaymentServiceV1 paymentService;
//    private final OrderServiceV1 orderServiceV1;
    @PostMapping("/v1/payments/confirm")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequest requestDto) {
        PaymentResponseDto response = paymentService.createPayment(requestDto);
        if( response == null){
            response.setStatus("FAIL");
        } else {
            response.setStatus("SUCCESS");
            response.setOrderId(requestDto.getOrderId());
            response.setTotalPrice(requestDto.getAmount());
            response.setApprovedAt(LocalDateTime.now());
        }
        return ResponseEntity.ok(response);
    }
}