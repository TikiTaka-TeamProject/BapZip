package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface ShopRepository{
    ShopEntity save(ShopEntity shop);
}
