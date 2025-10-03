package com.sparta.bapzip.shop.infrastructure.repository;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public abstract class ShopRepositoryImpl implements ShopRepository {
}
