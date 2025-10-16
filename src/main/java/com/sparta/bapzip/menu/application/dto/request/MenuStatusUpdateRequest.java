package com.sparta.bapzip.menu.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "메뉴 상태 수정 요청 DTO")
public record MenuStatusUpdateRequest(

        @Schema(description = "메뉴 상태", example = "AVAILABLE", allowableValues = {"AVAILABLE", "SOLD_OUT"})
        @NotNull(message = "메뉴 상태를 입력해주세요")
        String status
) {
}
