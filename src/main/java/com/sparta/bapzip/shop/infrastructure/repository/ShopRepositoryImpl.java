package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public ShopEntity save(ShopEntity shop) {
        return shopJpaRepository.save(shop);
    }

    @Override
    public boolean existsByOwnerId(Long ownerId) {
        return shopJpaRepository.existsByOwnerId(ownerId);
    }

    @Override
    public Optional<ShopEntity> findById(UUID shopId) {
        return shopJpaRepository.findById(shopId);
    }

    @Override
    public Page<ShopEntity> findByStatus(ShopStatusEnum status, Pageable pageable) {
        return shopJpaRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<ShopEntity> findAll(Pageable pageable) {
        return shopJpaRepository.findAll(pageable);
    }

    @Override
    public Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable) {
        return shopJpaRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
    }
    @Override
    public Optional<ShopEntity> findByIdAndIsDeletedFalse(UUID shopId) {
        return shopJpaRepository.findByIdAndIsDeletedFalse(shopId);
    }

    @Override
    public Page<ShopEntity> findByCategoryId(UUID categoryId, Pageable pageable) {
        return shopJpaRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<ShopEntity> findShopsByFilters(String name, UUID categoryId, Polygon areaPolygon, Pageable pageable) {
        return shopJpaRepository.findShopsByFilters(name, categoryId, areaPolygon, pageable);
    }

    @Override
    public Page<ShopEntity> findShopsWithoutPolygon(String name, UUID categoryId, Pageable pageable) {
        return shopJpaRepository.findShopsWithoutPolygon(name, categoryId, pageable);
    }

}
