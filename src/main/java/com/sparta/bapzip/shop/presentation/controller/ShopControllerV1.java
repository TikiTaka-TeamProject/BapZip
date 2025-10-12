package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.presentation.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.springframework.web.bind.annotation.RequestParam;
import com.sparta.bapzip.shop.presentation.dto.request.CreatShopRequest;
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
    public CreateShopResponse createShop(@RequestBody CreatShopRequest createShopRequest) {
        return shopServiceV1.createShop(createShopRequest);

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
            @RequestParam("ownerId") Long ownerId,
            @RequestBody ShopUpdateRequest shopUpdateRequest
//            @AuthenticationPrincipal UserDetails userDetails
    ) {
//        Long ownerId = userDetails.getId();
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
     * 특정 가게 삭제(soft delete) API
     * DELETE /v1/shops/{shopId}
     *
     * - 요청된 가게 ID와 소유자(ownerId)를 기준으로 가게를 조회
     * - 가게가 존재하지 않거나 이미 삭제된 경우 예외 발생
     * - 요청한 사용자가 가게 소유자가 아닐 경우 권한 예외 발생
     * - 실제 삭제는 soft delete 방식으로 isDeleted = true 처리
     *
     * @param shopId 삭제할 가게 ID
     * @param ownerId 요청한 사용자(Owner) ID, 권한 체크 용도
     * @return ResponseEntity<Void> 상태 코드 204(NO CONTENT) 반환, 실제 응답 바디 없음
     */
    @DeleteMapping("/{shopId}")
    public ResponseEntity<Void> deleteShop(
            @PathVariable UUID shopId,
            @RequestParam("ownerId") Long ownerId
//            @AuthenticationPrincipal UserPrincipal user // 또는 토큰에서 가져온 사용자 정보
    ) {
//        shopService.deleteShop(shopId, user.getId());
        shopServiceV1.deleteShop(shopId, ownerId);
        return ResponseEntity.noContent().build();

    }
}
