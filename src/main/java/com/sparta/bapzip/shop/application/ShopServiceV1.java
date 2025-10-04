package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {
    private final ShopRepository shopRepository;

    public ShopEntity getShopById(UUID shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new GlobalException(ErrorCode.SHOP_NOT_FOUND));
    }

    public void validateShopOwner(UUID shopId, Long ownerId) {
        ShopEntity shop = getShopById(shopId);

        // 권한 체크
        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new GlobalException(ErrorCode.UNAUTHORIZED_SHOP_ACCESS);
        }
    }
}
