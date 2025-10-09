package com.sparta.bapzip.category.infrastructure.repository;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Optional<CategoryEntity> findById(UUID categoryId) {
        return categoryJpaRepository.findById(categoryId);
    }
}
