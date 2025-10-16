package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.application.dto.ShopWithAvgScoreDto;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.locationtech.jts.geom.Polygon;
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
    Page<ShopEntity> findByStatus(ShopStatusEnum status, Pageable pageable);

    // 카테고리 ID로 조회
    Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);
    Page<ShopEntity> findAll(Pageable pageable);
    Optional<ShopEntity> findByIdAndIsDeletedFalse(UUID shopId);

    Page<ShopEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<ShopEntity> findShopsByFilters(String name, UUID categoryId, Polygon areaPolygon, Pageable pageable);

    Page<ShopEntity> findShopsWithoutPolygon(String name, UUID categoryId, Pageable pageable);

    Optional<ShopEntity> findShopWithAvgScore(UUID shopId);

    Page<ShopEntity> findShopsByPolygon(String name, UUID categoryId, Polygon areaPolygon, Pageable pageable);

    Page<ShopEntity> findShops(String name, UUID categoryId, Pageable pageable);

    List<ShopWithAvgScoreDto> findAllWithAvgScore();
}
