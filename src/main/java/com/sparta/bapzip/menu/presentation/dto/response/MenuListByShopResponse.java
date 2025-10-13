package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.enums.MenuStatus;

import java.util.List;
import java.util.UUID;

/**
 * 특정 가게 별 메뉴 목록 DTO -> 사용자 관점 설계
 * 가게, 메뉴 이름, 설명, 가격, 상태
 */
public record MenuListByShopResponse(
        UUID shopId,
        String shopName,
        List<MenuItemDto> menus
){
    // 가게 별 메뉴 정보 내부 클래스
    public record MenuItemDto(
            UUID id,
            String name,
            String content,
            int price,
            MenuStatus status
    ) {}
}