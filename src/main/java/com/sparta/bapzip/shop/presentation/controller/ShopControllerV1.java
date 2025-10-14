package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.presentation.dto.request.CreateShopRequest;
import com.sparta.bapzip.shop.presentation.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.springframework.web.bind.annotation.RequestParam;
import com.sparta.bapzip.shop.presentation.dto.response.CreateShopResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopControllerV1 {

    private final ShopServiceV1 shopServiceV1;

    @PostMapping
//    @PreAuthorize("hasRole('OWNER')")
    public CreateShopResponse createShop(
            @RequestBody CreateShopRequest createShopRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long ownerId = userDetails.getUser().getId();
        return shopServiceV1.createShop(createShopRequest, ownerId);

    }

    /**
     * 특정 가게 상세 정보 조회 API
     * GET /v1/shops/{shopId}
     *
     * @param shopId 조회할 가게 ID
     * @return ShopDetailResponse: 가게 상세 정보 DTO
     */
    @GetMapping("/{shopId}")
    public ShopDetailResponse getShopDetail(@PathVariable UUID shopId) {
        return shopServiceV1.getShopDetail(shopId);
    }


    @PatchMapping("/{shopId}")
    public ResponseEntity<ShopDetailResponse> updateShop(
            @PathVariable("shopId") UUID shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ShopUpdateRequest shopUpdateRequest

    ) {
        Long ownerId = userDetails.getUser().getId();
        ShopDetailResponse shopDetailResponse = shopServiceV1.updateShop(shopId, ownerId, shopUpdateRequest);
        return ResponseEntity.ok(shopDetailResponse);
    }

     /**
     * 승인된 가게 리스트 조회
     *
     * GET /v1/shops
     *
     * ShopServiceV1를 통해 승인 상태(APPROVED)인 가게들을 조회하고,
     * ShopDetailResponse DTO로 변환하여 반환
     *
     * @return ResponseEntity<List<ShopDetailResponse>> 승인된 가게 리스트
     */
    @GetMapping
    public List<ShopDetailForUserResponse> getApprovedShops(){
        return shopServiceV1.getApprovedShops()
                .stream()
                .map(ShopDetailForUserResponse::from) // DTO 변환 메서드 사용 가능
                .toList();
    }

    /**
     * 상태별 가게 목록 조회 API
     * GET /v1/shops/status
     *
     * @param shopStatusEnum 조회할 가게 상태 (ShopStatusEnum), 생략 가능
     *                       - null일 경우 모든 상태의 가게를 조회
     * @return List<ShopDetailResponse> 해당 상태(또는 전체)의 가게 상세 정보 리스트
     */
    @GetMapping("/status")
//    @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
    public List<ShopDetailResponse> getShopsByStatus(
            @RequestParam(value = "status", required = false) ShopStatusEnum shopStatusEnum
            ) {
        return shopServiceV1.getShopsByStatus(shopStatusEnum)
                .stream()
                .map(ShopDetailResponse::from)
                .toList();
    }

    /**
     * 가게 삭제 API (soft delete)
     * DELETE /v1/shops/{shopId}
     *
     * - 요청한 소유자만 삭제 가능
     * - 실제 삭제는 soft delete 방식(isDeleted = true)
     * - 삭제 후 응답은 HTTP 204 NO CONTENT
     *
     * @param shopId 삭제할 가게 UUID
     * @param userDetails 인증된 사용자 정보
     * @return ResponseEntity<Void> 상태 코드 204 반환
     */
    @DeleteMapping("/{shopId}")
    public ResponseEntity<Void> deleteShop(
            @PathVariable UUID shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long ownerId = userDetails.getUser().getId();
        shopServiceV1.deleteShop(shopId, ownerId);
        return ResponseEntity.noContent().build();

    }
}
