package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public List<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum) {
        return shopJpaRepository.findByStatus(shopStatusEnum);
    }
}
