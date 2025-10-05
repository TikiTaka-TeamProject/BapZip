package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;

import java.util.UUID;

public record MenuResponse(
        UUID id,
        String name,
        String content,
        int price,
        MenuStatus status,
        UUID shopId
) {
    public static MenuResponse from(MenuEntity menu){
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getContent(),
                menu.getPrice(),
                menu.getStatus(),
                menu.getShop().getId()
        );
    }
}
