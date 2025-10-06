package com.sparta.bapzip.category.domain.repository;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    // TODO: 용은 -> Shop 테스트작업을 위해 생성. 필요없을 시 제거
    Optional<CategoryEntity> findById(UUID categoryId);
}
