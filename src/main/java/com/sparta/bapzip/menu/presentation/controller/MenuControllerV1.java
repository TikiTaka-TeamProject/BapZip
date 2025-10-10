package com.sparta.bapzip.menu.presentation.controller;

import com.sparta.bapzip.global.common.dto.PageResponse;
import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.presentation.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuSearchRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuStatusUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.response.MenuCreateResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuDetailResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuListByShopResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/menus")
public class MenuControllerV1 {

    // todo: AuthenticationPrincipal, ApiResponse<>

    private final MenuServiceV1 menuService;

    /**
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<MenuCreateResponse> createMenu(@RequestBody @Valid MenuCreateRequest request){
        MenuCreateResponse menuCreateResponse = menuService.createMenu(request);
        return ResponseEntity.ok(menuCreateResponse);
    }

    /**
     * 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuById(@PathVariable UUID menuId){
        MenuDetailResponse menuDetailResponse = menuService.getMenuDetail(menuId);
        return ResponseEntity.ok(menuDetailResponse);
    }

    /**
     * 메뉴 검색 조회
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<MenuSearchResponse>> searchMenus(
            @Valid @ModelAttribute MenuSearchRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        // 페이징 검증 service 에서 처리
        Page<MenuSearchResponse> menuPage = menuService.searchMenus(request.keyword(), page, size, sortBy, isAsc);
        return ResponseEntity.ok(PageResponse.fromPage(menuPage, sortBy, isAsc));
    }

    /**
     * 가게 별 메뉴 조회
     * todo: url 매핑
     */
    @GetMapping("/{shopId}")
    public ResponseEntity<MenuListByShopResponse> getMenusByShop(@PathVariable UUID shopId){
        MenuListByShopResponse menuListByShop = menuService.getMenusByShop(shopId);
        return ResponseEntity.ok(menuListByShop);
    }

    /**
     * 메뉴 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<MenuSearchResponse>> getAllMenus(){
        List<MenuSearchResponse> menuList = menuService.getAllMenus();
        return ResponseEntity.ok(menuList);
    }

    /**
     * 메뉴 정보 수정
     */
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> updateMenu(@PathVariable UUID menuId,
                                                         @RequestBody @Valid MenuUpdateRequest request){
        MenuDetailResponse menuDetailResponse = menuService.updateMenu(menuId, request);
        return ResponseEntity.ok(menuDetailResponse);
    }

    /**
     * 메뉴 상태 수정
     */
    @PatchMapping("/{menuId}/status")
    public ResponseEntity<MenuDetailResponse> updateMenuStatus(@PathVariable UUID menuId,
                                                               @RequestBody @Valid MenuStatusUpdateRequest request){
        MenuDetailResponse MenuDetailResponse = menuService.updateMenuStatus(menuId, request);
        return ResponseEntity.ok(MenuDetailResponse);
    }
}
