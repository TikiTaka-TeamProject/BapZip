package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;

import java.util.UUID;

public record MenuDetailResponse(
        UUID id,
        String name,
        String content,
        int price,
        MenuStatus status,
        UUID shopId
) {
    public static MenuDetailResponse from(MenuEntity menu){
        return new MenuDetailResponse(
                menu.getId(),
                menu.getName(),
                menu.getContent(),
                menu.getPrice(),
                menu.getStatus(),
                menu.getShop().getId()
        );
    }
}
