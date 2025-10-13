package com.sparta.bapzip.category.domain.repository;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    public CategoryEntity save(CategoryEntity category);
    public List<CategoryEntity> findAllByIsDeletedFalse();

    Optional<CategoryEntity> findById(UUID categoryId);

    Optional<CategoryEntity> findByContent(String content);

    Optional<CategoryEntity> findByName(String name);
    public List<CategoryEntity> findAllForAdmin();

}
