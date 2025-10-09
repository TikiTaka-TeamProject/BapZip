package com.sparta.bapzip.menu.presentation.dto.request;

import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import jakarta.validation.constraints.NotNull;

public record MenuStatusUpdateRequest(
        @NotNull(message = "메뉴 상태를 입력해주세요")
        MenuStatus status
) {
}
