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

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopControllerV1 {

    private final ShopServiceV1 shopServiceV1;

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
    public ResponseEntity<List<ShopDetailForUserResponse>> getApprovedShops(){
        List<ShopDetailForUserResponse> shops = shopServiceV1.getApprovedShops()
                .stream()
                .map(shop -> ShopDetailForUserResponse.builder()
                        .shopId(shop.getId())
                        .name(shop.getName())
                        .address(shop.getAddress())
                        .ownerName(shop.getOwner().getName())
                        .categoryName(shop.getCategory().getName())
                        .serviceAreaName(shop.getServiceArea().getName())
                        .build())
                .toList();

        return ResponseEntity.ok(shops);
    }
}
