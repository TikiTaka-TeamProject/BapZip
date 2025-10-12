package com.sparta.bapzip.category.presentation.controller;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.presentation.dto.request.CategoryRequestDto;
import com.sparta.bapzip.category.presentation.dto.response.CategoryDetailResponse;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryControllerV1 {
    private final CategoryServiceV1 categoryServiceV1;

    // 삭제되지 않은 카테고리 리스트 조회
    @GetMapping
    public ResponseEntity<List<CategoryDetailResponse>> getActiveCategories() {
        List<CategoryDetailResponse> categories = categoryServiceV1.getActiveCategories();
        return ResponseEntity.ok(categories);
    }

    //  MASTER, MANAGER만 접근 가능
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public ResponseEntity<List<CategoryDetailResponse>> getAllCategoriesForAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(categoryServiceV1.getAllCategories());
    }
    // 카테고리 ID 기준 가게 리스트 조회
    @GetMapping("/{categoryId}/shops")
    public Page<ShopDetailForUserResponse> getShopsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc
    ) {
        return categoryServiceV1.getShopsByCategory(categoryId, page, size, sortBy, isAsc);
    }

    // 카테고리 생성
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<CategoryDetailResponse> createCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CategoryRequestDto request) {
        CategoryDetailResponse categoryDetailResponse = categoryServiceV1.createCategory(request.getName(), request.getContent(), userDetails.getUser().getId());
        return ResponseEntity.ok(categoryDetailResponse);
    }
    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<CategoryDetailResponse> updateCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable UUID categoryId,@RequestBody CategoryRequestDto request) {
        CategoryDetailResponse categoryDetailResponse = categoryServiceV1.updateCategory(categoryId, request.getName(), request.getContent(), userDetails.getUser().getId());
        return ResponseEntity.ok(categoryDetailResponse);
    }

    // 카테고리 삭제 (Soft Delete)
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    public ResponseEntity<CategoryDetailResponse> deleteCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable UUID categoryId) {
        CategoryDetailResponse categoryDetailResponse = categoryServiceV1.deleteCategory(categoryId, userDetails.getUser().getId());
        return ResponseEntity.ok(categoryDetailResponse);
    }
}
