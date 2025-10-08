package com.sparta.bapzip.category.infrastructure.repository;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findAllByIsDeletedFalse();
}
