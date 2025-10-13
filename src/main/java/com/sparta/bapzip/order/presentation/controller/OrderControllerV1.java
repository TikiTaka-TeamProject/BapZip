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
    public ResponseEntity<OrderDetailResponse> getOrderById(
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
    public ResponseEntity<Page<ShopOrderResponse>> getOrderByShopId(
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

    /**
     * 주문 수락
     */
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.acceptOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    /**
     * 조리 시작
     */
    @PostMapping("/{orderId}/start-cooking")
    public ResponseEntity<Void> startCooking(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.startCooking(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    /**
     * 조리 완료
     */
    @PostMapping("/{orderId}/complete-cooking")
    public ResponseEntity<Void> completeCooking(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.completeCooking(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    /**
     * 배달 시작
     */
    @PostMapping("/{orderId}/start-delivery")
    public ResponseEntity<Void> startDelivery(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.startDelivery(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    /**
     * 배달 완료
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<Void> completeDelivery(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.completeDelivery(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    /**
     * 주문 취소
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.cancelOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    /**
     * 조리 완료
     */
    @PostMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.rejectOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}