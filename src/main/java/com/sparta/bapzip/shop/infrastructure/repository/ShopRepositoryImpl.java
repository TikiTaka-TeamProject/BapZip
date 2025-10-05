package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
