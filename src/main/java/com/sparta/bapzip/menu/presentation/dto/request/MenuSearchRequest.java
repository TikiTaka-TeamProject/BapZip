package com.sparta.bapzip.menu.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MenuSearchRequest(
        @NotBlank(message="검색어를 입력해 주세요.")
        String keyword
) {
}
