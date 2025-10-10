package com.sparta.bapzip.category.infrastructure.repository;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;
    @Override
    public CategoryEntity save(CategoryEntity category) {
        return categoryJpaRepository.save(category);
    }
    @Override
    public List<CategoryEntity> findAllByIsDeletedFalse() {
        return categoryJpaRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Optional<CategoryEntity> findById(UUID categoryId) {
        return categoryJpaRepository.findById(categoryId);
    }

    @Override
    public Optional<CategoryEntity> findByContent(String content) {
        return categoryJpaRepository.findByContent(content);
    }

    @Override
    public Optional<CategoryEntity> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }
}
