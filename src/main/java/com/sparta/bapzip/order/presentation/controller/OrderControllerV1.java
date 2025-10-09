package com.sparta.bapzip.order.presentation.controller;

import com.sparta.bapzip.order.application.OrderServiceV1;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.order.presentation.dto.response.CreateOrderResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderServiceV1;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest createOrderRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateOrderResponse.from(
                        orderServiceV1.createOrder(createOrderRequest, userDetails.getUser())
                ));
    }
}