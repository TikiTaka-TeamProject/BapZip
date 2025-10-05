package com.sparta.bapzip.menu.presentation.controller;

import com.sparta.bapzip.menu.application.MenuServiceV1;
import com.sparta.bapzip.menu.presentation.dto.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/menus")
public class MenuControllerV1 {

    // todo: AuthenticationPrincipal, ApiResponse<>

    private final MenuServiceV1 menuService;

    /**
     * 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable UUID menuId){
        MenuResponse menuResponse = menuService.getMenuById(menuId);
        return ResponseEntity.ok().body(menuResponse);
    }
}
