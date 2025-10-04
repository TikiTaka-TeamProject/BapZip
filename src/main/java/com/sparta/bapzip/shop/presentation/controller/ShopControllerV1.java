package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopControllerV1 {

    private final ShopServiceV1 shopServiceV1;

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
