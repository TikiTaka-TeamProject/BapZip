package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "메뉴 검색 조회 응답 DTO")
public record MenuSearchResponse(

        @Schema(description = "메뉴 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "메뉴 이름", example = "불고기 덮밥")
        String name,

        @Schema(description = "메뉴 가격", example = "8500")
        int price,

        @Schema(description = "가게 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID shopId,

        @Schema(description = "가게 이름", example = "한식당 불고기")
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
