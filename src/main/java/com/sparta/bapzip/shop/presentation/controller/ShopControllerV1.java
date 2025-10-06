package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sparta.bapzip.shop.presentation.dto.request.CreatShopRequest;
import com.sparta.bapzip.shop.presentation.dto.response.CreateShopResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/status")
//    @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
    public ResponseEntity<List<ShopDetailResponse>> getShopsByStatus(
            @RequestParam("status")ShopStatusEnum shopStatusEnum
            ) {
        List<ShopDetailResponse> shops = shopServiceV1.getShopsByStatus(shopStatusEnum)
                .stream()
                .map(shop -> ShopDetailResponse.builder()
                        .shopId(shop.getId())
                        .name(shop.getName())
                        .address(shop.getAddress())
                        .status(shop.getStatus())
                        .ownerName(shop.getOwner().getName())
                        .categoryName(shop.getCategory().getName())
                        .serviceAreaName(shop.getServiceArea().getName())
                        .build())
                .toList();

        return ResponseEntity.ok(shops);
    }
}
