package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public List<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum) {
        return shopJpaRepository.findByStatus(shopStatusEnum);
    }

    @Override
    public List<ShopEntity> findAll() {
        return shopJpaRepository.findAll();
    }

    @Override
    public Page<ShopEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable) {
        return shopJpaRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
    }
    @Override
    public Optional<ShopEntity> findByIdAndIsDeletedFalse(UUID shopId) {
        return shopJpaRepository.findByIdAndIsDeletedFalse(shopId);
    }
}
