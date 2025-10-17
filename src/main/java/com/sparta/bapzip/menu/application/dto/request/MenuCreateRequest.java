package com.sparta.bapzip.menu.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Schema(description = "메뉴 생성 요청 DTO")
public record MenuCreateRequest(

        @Schema(description = "메뉴 이름", example = "불고기 덮밥")
        @NotBlank(message = "메뉴 이름을 입력해 주세요.")
        String name,

        @Schema(description = "메뉴 설명", example = "달콤한 불고기와 밥이 어우러진 메뉴")
        @NotBlank(message = "메뉴 설명을 입력해 주세요.")
        @Size(max = 255, message = "내용은 최대 255자 이내로 작성해야 합니다.")
        String content,

        @Schema(description = "메뉴 가격", example = "8500")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Max(value = 9999999, message = "메뉴 가격의 허용 범위를 초과했습니다.")
        int price,

        @Schema(description = "가게 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "가게 정보가 없습니다.")
        UUID shopId
) {}
