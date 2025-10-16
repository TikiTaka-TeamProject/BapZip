package com.sparta.bapzip.menu.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "메뉴 검색 요청 DTO")
public record MenuSearchRequest(

        @Schema(description = "검색 키워드", example = "불고기")
        @NotBlank(message="검색어를 입력해 주세요.")
        String keyword
) {
}
