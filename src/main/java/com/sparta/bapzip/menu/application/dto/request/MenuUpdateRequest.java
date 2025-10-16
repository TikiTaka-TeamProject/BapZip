package com.sparta.bapzip.menu.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Schema(description = "메뉴 수정 요청 DTO")
public record MenuUpdateRequest(
        @Schema(description = "메뉴 이름", example = "매운 불고기 덮밥")
        String name,

        @Schema(description = "메뉴 설명", example = "매콤하게 조리된 불고기 덮밥")
        @Size(max = 255, message = "내용은 최대 255자 이내로 작성해야 합니다.")
        String content,

        // update Integer => null값 허용 (PATCH)
        @Schema(description = "메뉴 가격", example = "9000")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Max(value = 9999999, message = "메뉴 가격의 허용 범위를 초과했습니다.")
        Integer price
) {
}
