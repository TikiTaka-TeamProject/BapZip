package com.sparta.bapzip.menu.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.global.response.PageResponseDto;
import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.application.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.application.dto.request.MenuSearchRequest;
import com.sparta.bapzip.menu.application.dto.request.MenuStatusUpdateRequest;
import com.sparta.bapzip.menu.application.dto.request.MenuUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.response.*;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name="메뉴 api", description = "메뉴 관련 api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/menus")
public class MenuControllerV1 {

    private final MenuServiceV1 menuService;


    /**
     * 메뉴 생성 - OWNER
     */
    @Operation(summary = "메뉴 등록", description = "가게 Owner가 새로운 메뉴를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<MenuCreateResponse>> createMenu(@RequestBody @Valid MenuCreateRequest request,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long ownerId = userDetails.getUser().getId(); // ownerId만 검증

        MenuCreateResponse menuCreateResponse = menuService.createMenu(request, ownerId);
        return ApiResponse.created(menuCreateResponse);
    }

    /**
     * 메뉴 상세 조회
     */
    @Operation(summary = "메뉴 상세 조회", description = "생성된 메뉴 상세 조회 메서드입니다.(단일 조회)")
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuDetailResponse>> getMenuById(@PathVariable UUID menuId){
        MenuDetailResponse menuDetailResponse = menuService.getMenuDetail(menuId);
        return ApiResponse.ok(menuDetailResponse);
    }

    /**
     * 메뉴 검색 조회
     */
    @Operation(summary = "메뉴 이름 검색 조회", description = "메뉴 이름으로 검색하여 페이징된 결과를 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponseDto<MenuSearchResponse>>> searchMenus(
            @Valid @ModelAttribute MenuSearchRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        // 페이징 검증 service 에서 처리
        Page<MenuSearchResponse> menuPage = menuService.searchMenus(request.keyword(), page, size, sortBy, isAsc);
        return ApiResponse.ok(PageResponseDto.fromPage(menuPage, sortBy, isAsc));
    }

    /**
     * 가게 별 메뉴 조회
     * v1/menus?shopId=
     */
    @Operation(summary = "가게별 메뉴 조회", description = "특정 가게에 등록된 메뉴를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<MenuListByShopResponse>> getMenusByShop(@RequestParam UUID shopId){
        MenuListByShopResponse menuListByShop = menuService.getMenusByShop(shopId);
        return ApiResponse.ok(menuListByShop);
    }

    /**
     * 메뉴 전체 조회 - MANAGER, MASTER (관리자용)
     * soft delete 처리 된 메뉴까지 조회 가능
     */
    @Operation(summary = "전체 메뉴 조회(관리자용)", description = "관리자가 모든 메뉴를 조회합니다. (soft delete 처리 된 메뉴 포함)")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<PageResponseDto<MenuAdminResponse>>> getAllMenus(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        Page<MenuAdminResponse> menuPage = menuService.getAllMenus(page, size, sortBy, isAsc);
        return ApiResponse.ok(PageResponseDto.fromPage(menuPage, sortBy, isAsc));
    }

    /**
     * 메뉴 정보 수정 - OWNER
     */
    @Operation(summary = "메뉴 정보 수정", description = "가게 Owner가 메뉴 정보를 수정합니다.")
    @PatchMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuDetailResponse>> updateMenu(@PathVariable UUID menuId,
                                                         @RequestBody @Valid MenuUpdateRequest request,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long ownerId = userDetails.getUser().getId();
        MenuDetailResponse menuDetailResponse = menuService.updateMenu(menuId, request, ownerId);
        return ApiResponse.ok(menuDetailResponse);
    }

    /**
     * 메뉴 상태 수정 - OWNER
     */
    @Operation(summary = "메뉴 상태 수정", description = "가게 Owner가 메뉴 상태를 수정합니다.")
    @PatchMapping("/{menuId}/status")
    public ResponseEntity<ApiResponse<MenuDetailResponse>> updateMenuStatus(@PathVariable UUID menuId,
                                                               @RequestBody @Valid MenuStatusUpdateRequest request,
                                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long ownerId = userDetails.getUser().getId();
        MenuDetailResponse menuDetailResponse = menuService.updateMenuStatus(menuId, request, ownerId);
        return ApiResponse.ok(menuDetailResponse);
    }

    /**
     * 메뉴 삭제 - OWNER
     */
    @Operation(summary = "메뉴 삭제", description = "가게 Owner가 메뉴를 삭제합니다.")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable UUID menuId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long ownerId = userDetails.getUser().getId();
        menuService.deleteMenu(menuId, ownerId);
        return ApiResponse.noContent();
    }
}
