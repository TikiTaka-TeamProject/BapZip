package com.sparta.bapzip.menu.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record MenuUpdateRequest(
        String name,

        @Size(max = 255, message = "내용은 최대 255자 이내로 작성해야 합니다.")
        String content,

        // update Integer => null값 허용 (PATCH)
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Max(value = 9999999, message = "메뉴 가격의 허용 범위를 초과했습니다.")
        Integer price
) {
}
