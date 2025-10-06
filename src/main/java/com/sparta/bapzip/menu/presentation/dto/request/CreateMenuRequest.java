package com.sparta.bapzip.menu.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateMenuRequest(

        @NotBlank(message = "메뉴 이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "메뉴 설명을 입력해 주세요.")
        @Size(max = 255, message = "내용은 최대 255자 이내로 작성해야 합니다.")
        String content,

        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Max(value = 9999999, message = "메뉴 가격의 허용 범위를 초과했습니다.")
        int price,

        @NotNull(message = "가게 정보가 없습니다.")
        UUID shopId
) {}
