package com.sparta.bapzip.menu.presentation.controller;

import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.presentation.dto.request.CreateMenuRequest;
import com.sparta.bapzip.menu.presentation.dto.response.CreateMenuResponse;
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
    public ResponseEntity<CreateMenuResponse> createMenu(@RequestBody @Valid CreateMenuRequest request){
        CreateMenuResponse createMenuResponse = menuService.createMenu(request);
        return ResponseEntity.ok().body(createMenuResponse);
    }

    /**
     * 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuById(@PathVariable UUID menuId){
        MenuDetailResponse menuDetailResponse = menuService.getMenuDetail(menuId);
        return ResponseEntity.ok().body(menuDetailResponse);
    }
}
