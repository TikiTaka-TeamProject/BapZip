package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, UUID> {
    boolean existsByOwnerId(Long ownerId);

    @NonNull
    Optional<ShopEntity> findById(@NonNull UUID shopId);

}
