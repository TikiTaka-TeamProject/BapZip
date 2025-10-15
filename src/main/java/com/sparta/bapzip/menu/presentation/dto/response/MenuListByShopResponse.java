package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;

import java.util.List;
import java.util.UUID;

/**
 * 특정 가게 별 메뉴 조회
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
    ) {
        public static MenuItemDto from(MenuEntity menu) {
            return new MenuItemDto(
                    menu.getId(),
                    menu.getName(),
                    menu.getContent(),
                    menu.getPrice(),
                    menu.getStatus()
            );
        }
    }
}