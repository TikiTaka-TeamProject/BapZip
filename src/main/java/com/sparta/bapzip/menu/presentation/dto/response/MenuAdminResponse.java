package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;

import java.util.UUID;

public record MenuAdminResponse(
        UUID id,
        String name,
        String content,
        int price,
        MenuStatus status,
        boolean isDeleted,
        UUID shopId
) {
    public static MenuAdminResponse from(MenuEntity menu) {
        return new MenuAdminResponse(
                menu.getId(),
                menu.getName(),
                menu.getContent(),
                menu.getPrice(),
                menu.getStatus(),
                menu.getIsDeleted(),
                menu.getShop().getId()
        );
    }
}
