package com.sparta.bapzip.order.presentation.controller;

import com.sparta.bapzip.order.application.OrderServiceV1;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.order.presentation.dto.response.OrderDetailResponse;
import com.sparta.bapzip.order.presentation.dto.response.OrderResponse;
import com.sparta.bapzip.order.presentation.dto.response.CreateOrderResponse;
import com.sparta.bapzip.order.presentation.dto.response.ShopOrderResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderServiceV1;

    /**
     * 특정 유저 주문 전체 조회
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable)
    {
        return ResponseEntity.ok(
                orderServiceV1.getOrdersByUser(userDetails.getUser(), pageable)
                        .map(OrderResponse::from)
        );
    }

    /**
     * 특정 유저의 주문 상세내역 조회
     */
    @GetMapping(params = "orderId")
    public ResponseEntity<?> getOrderById(
            @RequestParam UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return ResponseEntity.ok(
                (OrderDetailResponse
                        .from(orderServiceV1.getOrderById(orderId, userDetails.getUser()))
                )
        );
    }

    /**
     * 특정 가게의 주문 전체 조회
     */
    @GetMapping(params = "shopId")
    public ResponseEntity<Page<?>> getOrderByShopId(
            @RequestParam UUID shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable)
    {
        return ResponseEntity.ok(
                orderServiceV1.getOrderByShopId(shopId, userDetails.getUser(), pageable)
                        .map(ShopOrderResponse::from)
        );
    }

    /**
     * 주문 등록
     */
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