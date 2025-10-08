package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, UUID> {
    boolean existsByOwnerId(Long ownerId);

    @NonNull
    Optional<ShopEntity> findById(@NonNull UUID shopId);

    List<ShopEntity> findByStatus(ShopStatusEnum shopStatusEnum);

    List<ShopEntity> findAllByCategoryIdAndIsDeletedFalse(UUID categoryId);
}
