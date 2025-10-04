package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, UUID> {
}