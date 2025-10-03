package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;

import java.util.Optional;
import java.util.UUID;

public interface ShopRepository {
    // 가게 ID로 조회
    Optional<ShopEntity> findById(UUID shopId);
}
