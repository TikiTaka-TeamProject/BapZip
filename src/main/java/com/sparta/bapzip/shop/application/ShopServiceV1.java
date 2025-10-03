package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {
    private final ShopRepository shopRepository;

    /**
     * 가게 ID를 기준으로 ShopEntity 조회
     *
     * @param shopId 조회할 가게의 UUID
     * @return 조회된 ShopEntity
     * @throws GlobalException SHOP_NOT_FOUND 에러 발생 시
     */
    public ShopEntity getShopById(UUID shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new GlobalException(ErrorCode.SHOP_NOT_FOUND));
    }

    /**
     * 가게 ID 기준으로 상세 정보 조회
     * ShopEntity를 조회 후 필요한 정보를 ShopDetailResponse로 변환
     *
     * @param shopId 조회할 가게의 UUID
     * @return ShopDetailResponse 가게 상세 정보 DTO
     * @throws GlobalException SHOP_NOT_FOUND 에러 발생 시
     */
    public ShopDetailResponse getShopDetail(UUID shopId) {
        ShopEntity shop = getShopById(shopId);

        return ShopDetailResponse.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .status(shop.getStatus())
                .ownerName(shop.getOwner().getName())
                .categoryName(shop.getCategory().getName())
                .serviceAreaName(shop.getServiceArea().getName())
                .build();
    }
}
