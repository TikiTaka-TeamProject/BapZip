package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
