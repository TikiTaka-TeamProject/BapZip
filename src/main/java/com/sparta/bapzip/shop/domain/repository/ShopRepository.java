package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShopRepository {
    ShopEntity save(ShopEntity shop);
    boolean existsByOwnerId(Long ownerId);
    // 가게 ID로 조회
    Optional<ShopEntity> findById(UUID shopId);
    List<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum);

    // 카테고리 ID로 조회
    Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);
    List<ShopEntity> findAll();
}
