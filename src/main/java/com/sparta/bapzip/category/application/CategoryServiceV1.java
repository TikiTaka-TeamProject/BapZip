package com.sparta.bapzip.category.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.exception.CategoryException;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.category.presentation.dto.response.CategoryDetailResponse;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
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
    public Page<ShopDetailForUserResponse> getShopsByCategory(UUID categoryId, int page, int size, String sortBy, boolean isAsc) {
        int validatedSize = List.of(10, 30, 50).contains(size) ? size : 10;
        String validatedSortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, validatedSize, Sort.by(direction, validatedSortBy));
        Page<ShopEntity> shopPage = shopRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);

        return shopPage.map(ShopDetailForUserResponse::from);
    }

    // 카테고리 생성
    @Transactional
    public CategoryDetailResponse createCategory (String name, String content, UserDetailsImpl userDetails){
        if (categoryRepository.findByName(name).isPresent() || categoryRepository.findByContent(content).isPresent()) {
            throw new CategoryException(ErrorCode.DUPLICATE_CATEGORY_EXCEPTION);
        }
        CategoryEntity category = new CategoryEntity(name, content);
        category.markCreated(userDetails.getUser().getId());
        categoryRepository.save(category);
        return CategoryDetailResponse.toDto(category);
    }

    @Transactional
    public CategoryDetailResponse updateCategory(UUID categoryId, String name, String content, UserDetailsImpl userDetails) {
        CategoryEntity category = getCategoryById(categoryId);
        category.update(name, content);
        category.markUpdated(userDetails.getUser().getId());
        categoryRepository.save(category);
        return CategoryDetailResponse.toDto(category);
    }

    // 카테고리 삭제
    @Transactional
    public CategoryDetailResponse deleteCategory(UUID categoryId, UserDetailsImpl userDetails) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
        if (category.getIsDeleted()) {
            throw new CategoryException(ErrorCode.INVALID_CATEGORY_ID);
        }
        category.markDeleted(userDetails.getUser().getId());
        categoryRepository.save(category);
        return CategoryDetailResponse.toDto(category);
    }

    public List<CategoryDetailResponse> getAllCategories() {
        return categoryRepository.findAllForAdmin()
                .stream()
                .map(CategoryDetailResponse::toDtoForAdmin)
                .toList();
    }
}
