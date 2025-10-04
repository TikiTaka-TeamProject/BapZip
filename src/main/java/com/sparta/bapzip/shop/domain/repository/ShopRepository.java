package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;

import java.util.List;

public interface ShopRepository {
    List<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum);
}
