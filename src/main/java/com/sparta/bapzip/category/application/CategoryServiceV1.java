package com.sparta.bapzip.category.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.category.presentation.dto.response.CategoryDetailResponse;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceV1 {

    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    public CategoryEntity getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
    }
    // 삭제되지 않은 카테고리 리스트 조회
    @Transactional(readOnly = true)
    public List<CategoryDetailResponse> getActiveCategories() {
        return categoryRepository.findAllByIsDeletedFalse()
                .stream()
                .map(CategoryDetailResponse::toDto)
                .toList();
    }

    // 카테고리 ID 기준 가게 리스트 조회
    @Transactional(readOnly = true)
    public List<ShopDetailForUserResponse> getShopsByCategory (UUID categoryId){
        return shopRepository.findAllByCategoryIdAndIsDeletedFalse(categoryId)
                .stream()
                .map(ShopDetailForUserResponse::from)
                .toList();
    }

    // 카테고리 생성
    @Transactional
    public CategoryDetailResponse createCategory (String name, String content, Long userId){
        CategoryEntity category = new CategoryEntity(name, content);
        category.markCreated(userId);
        categoryRepository.save(category);
        return CategoryDetailResponse.toDto(category);
    }

    @Transactional
    public CategoryDetailResponse updateCategory(UUID categoryId, String name, String content, Long userId) {
        CategoryEntity category = getCategoryById(categoryId);
        category.update(name, content);
        category.markUpdated(userId);
        categoryRepository.save(category);
        return CategoryDetailResponse.toDto(category);
    }

    // 카테고리 삭제
    @Transactional
    public CategoryDetailResponse deleteCategory(UUID categoryId, Long userId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
        category.markDeleted(userId);
        categoryRepository.save(category);
        return CategoryDetailResponse.toDto(category);
    }
}
