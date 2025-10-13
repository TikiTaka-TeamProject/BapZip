package com.sparta.bapzip.order.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
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
            Pageable pageable) {
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
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
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
            Pageable pageable) {
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
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateOrderResponse.from(
                        orderServiceV1.createOrder(createOrderRequest, userDetails.getUser())
                ));
    }

    /**
     * 주문 수락
     */
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<ApiResponse<String>> acceptOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.acceptOrder(orderId, userDetails.getUser());
        return ApiResponse.ok("주문이 수락되었습니다.");
    }

    /**
     * 조리 시작
     */
    @PutMapping("/{orderId}/start-cooking")
    public ResponseEntity<ApiResponse<String>> startCooking(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.startCooking(orderId, userDetails.getUser());
        return ApiResponse.ok("조리를 시작했습니다.");
    }

    /**
     * 조리 완료
     */
    @PutMapping("/{orderId}/complete-cooking")
    public ResponseEntity<ApiResponse<String>> completeCooking(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.completeCooking(orderId, userDetails.getUser());
        return ApiResponse.ok("조리가 완료되었습니다.");
    }

    /**
     * 배달 시작
     */
    @PutMapping("/{orderId}/start-delivery")
    public ResponseEntity<ApiResponse<String>> startDelivery(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.startDelivery(orderId, userDetails.getUser());
        return ApiResponse.ok("배달을 시작했습니다.");
    }

    /**
     * 배달 완료
     */
    @PutMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponse<String>> completeDelivery(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.completeDelivery(orderId, userDetails.getUser());
        return ApiResponse.ok("배달이 완료되었습니다.");
    }

    /**
     * 주문 거절
     */
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.rejectOrder(orderId, userDetails.getUser());
        return ApiResponse.ok("주문이 거절되었습니다.");
    }

    /**
     * 주문 취소
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.cancelOrder(orderId, userDetails.getUser());
        return ApiResponse.ok("주문이 취소되었습니다.");
    }

    /**
     * 주문 내역 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.delete(orderId, userDetails.getUser());
        return ApiResponse.ok("주문 내역이 삭제되었습니다.");
    }
}