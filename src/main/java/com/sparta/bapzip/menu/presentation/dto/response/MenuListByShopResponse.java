package com.sparta.bapzip.menu.presentation.dto.response;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "특정 가게별 메뉴 조회 응답 DTO")
public record MenuListByShopResponse(

        @Schema(description = "가게 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID shopId,

        @Schema(description = "가게 이름", example = "한식당 불고기")
        String shopName,

        @Schema(description = "가게 메뉴 목록")
        List<MenuItemDto> menus
){
    @Schema(description = "가게 메뉴 항목 DTO")
    public record MenuItemDto(

            @Schema(description = "메뉴 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
            UUID id,

            @Schema(description = "메뉴 이름", example = "불고기 덮밥")
            String name,

            @Schema(description = "메뉴 설명", example = "달콤한 불고기와 밥이 어우러진 메뉴")
            String content,

            @Schema(description = "메뉴 가격", example = "8500")
            int price,

            @Schema(description = "메뉴 상태", example = "AVAILABLE")
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