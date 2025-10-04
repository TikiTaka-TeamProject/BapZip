package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.presentation.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopControllerV1 {

    private final ShopServiceV1 shopServiceV1;

    @PutMapping("/{shopId}")
    public ResponseEntity<ShopDetailResponse> updateShop(
            @PathVariable("shopId") UUID shopId,
            @RequestParam("ownerId") Long ownerId,
            @RequestBody ShopUpdateRequest shopUpdateRequest
//            @AuthenticationPrincipal UserDetails userDetails
    ){
//        Long ownerId = userDetails.getId();
        ShopDetailResponse shopDetailResponse = shopServiceV1.updateShop(shopId, ownerId, shopUpdateRequest);
        return ResponseEntity.ok(shopDetailResponse);
    }
}
