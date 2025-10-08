package com.sparta.bapzip.menu.presentation.controller;

import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.presentation.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.response.MenuCreateResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 메뉴 정보 수정
     */
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> updateMenu(@PathVariable UUID menuId,
                                                         @RequestBody @Valid MenuUpdateRequest request){
        MenuDetailResponse menuDetailResponse = menuService.updateMenu(menuId, request);
        return ResponseEntity.ok(menuDetailResponse);
    }
}
