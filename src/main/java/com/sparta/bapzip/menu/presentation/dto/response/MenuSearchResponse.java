package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;

import java.util.UUID;

/**
 * 배달앱을 참고한 필드
 * 메뉴 이름, 가격, 가게 이름, 필요 시 상태 + 리뷰 구현 시 가게 평점
 */
public record MenuSearchResponse(
        UUID id,
        String name,
        int price,
        UUID shopId,
        String shopName
) {
    public static MenuSearchResponse from(MenuEntity menu) {
        return new MenuSearchResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getShop().getId(),
                menu.getShop().getName()
        );
    }
}
