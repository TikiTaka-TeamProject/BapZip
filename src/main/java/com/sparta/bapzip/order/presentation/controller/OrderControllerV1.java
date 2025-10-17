package com.sparta.bapzip.order.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.order.application.OrderServiceV1;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import com.sparta.bapzip.order.presentation.dto.response.OrderDetailResponse;
import com.sparta.bapzip.order.presentation.dto.response.OrderResponse;
import com.sparta.bapzip.order.presentation.dto.response.CreateOrderResponse;
import com.sparta.bapzip.order.presentation.dto.response.ShopOrderResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "주문 관리", description = "주문 생성, 조회, 상태 변경 및 삭제 API")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderServiceV1;

    @Operation(summary = "사용자 주문 전체 조회", description = "특정 사용자의 모든 주문 내역을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        Page<OrderResponse> orders = orderServiceV1.getOrdersByUser(userDetails.getUser(), pageable)
                .map(OrderResponse::from);
        return ApiResponse.ok(orders);
    }

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 내역을 조회합니다.")
    @GetMapping(params = "orderId")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderById(
            @RequestParam UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderDetailResponse orderDetail = OrderDetailResponse
                .from(orderServiceV1.getOrderById(orderId, userDetails.getUser()));
        return ApiResponse.ok(orderDetail);
    }

    @Operation(summary = "가게별 주문 조회", description = "특정 가게의 모든 주문 내역을 페이징하여 조회합니다.")
    @GetMapping(params = "shopId")
    public ResponseEntity<ApiResponse<Page<ShopOrderResponse>>> getOrderByShopId(
            @RequestParam UUID shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        Page<ShopOrderResponse> shopOrders = orderServiceV1.getOrderByShopId(shopId, userDetails.getUser(), pageable)
                .map(ShopOrderResponse::from);
        return ApiResponse.ok(shopOrders);
    }

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest createOrderRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CreateOrderResponse response = CreateOrderResponse.from(
                orderServiceV1.createOrder(createOrderRequest, userDetails.getUser())
        );

        return ApiResponse.created(response);
    }

    @Operation(summary = "주문 수락", description = "가게 주인이 주문을 수락합니다.")
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<ApiResponse<String>> acceptOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.acceptOrder(orderId, userDetails.getUser());
        return ApiResponse.ok("주문이 수락되었습니다.");
    }

    @Operation(summary = "조리 시작", description = "주문의 조리를 시작합니다.")
    @PutMapping("/{orderId}/start-cooking")
    public ResponseEntity<ApiResponse<String>> startCooking(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.startCooking(orderId, userDetails.getUser());
        return ApiResponse.ok("조리를 시작했습니다.");
    }

    @Operation(summary = "조리 완료", description = "주문의 조리를 완료합니다.")
    @PutMapping("/{orderId}/complete-cooking")
    public ResponseEntity<ApiResponse<String>> completeCooking(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.completeCooking(orderId, userDetails.getUser());
        return ApiResponse.ok("조리가 완료되었습니다.");
    }

    @Operation(summary = "배달 시작", description = "주문의 배달을 시작합니다.")
    @PutMapping("/{orderId}/start-delivery")
    public ResponseEntity<ApiResponse<String>> startDelivery(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.startDelivery(orderId, userDetails.getUser());
        return ApiResponse.ok("배달을 시작했습니다.");
    }

    @Operation(summary = "배달 완료", description = "주문의 배달을 완료합니다.")
    @PutMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponse<String>> completeDelivery(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.completeDelivery(orderId, userDetails.getUser());
        return ApiResponse.ok("배달이 완료되었습니다.");
    }

    @Operation(summary = "주문 거절", description = "가게 주인이 주문을 거절합니다.")
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.rejectOrder(orderId, userDetails.getUser());
        return ApiResponse.ok("주문이 거절되었습니다.");
    }

    @Operation(summary = "주문 취소", description = "고객이 주문을 취소합니다.")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.cancelOrder(orderId, userDetails.getUser());
        return ApiResponse.ok("주문이 취소되었습니다.");
    }

    @Operation(summary = "주문 내역 삭제", description = "주문 내역을 삭제합니다.")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderServiceV1.delete(orderId, userDetails.getUser());
        return ApiResponse.ok("주문 내역이 삭제되었습니다.");
    }
}