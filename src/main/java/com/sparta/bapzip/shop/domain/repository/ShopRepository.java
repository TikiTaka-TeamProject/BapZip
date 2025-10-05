package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;

public interface ShopRepository{
    ShopEntity save(ShopEntity shop);
    boolean existsByOwnerId(Long ownerId);
}
