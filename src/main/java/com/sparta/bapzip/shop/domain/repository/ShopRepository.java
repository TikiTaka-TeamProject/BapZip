package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;

import java.util.List;


import java.util.Optional;
import java.util.UUID;

public interface ShopRepository {
    ShopEntity save(ShopEntity shop);
    boolean existsByOwnerId(Long ownerId);
    // 가게 ID로 조회
    Optional<ShopEntity> findById(UUID shopId);
    List<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum);
}
