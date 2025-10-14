package com.sparta.bapzip.menu.domain.enums;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.menu.domain.exception.InvalidMenuStatusException;

public enum MenuStatus {
    AVAILABLE,   // 판매중
    SOLD_OUT;     // 매진

    public static MenuStatus from(String value) {
        try {
            return MenuStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidMenuStatusException(ErrorCode.INVALID_MENU_STATUS);
        }
    }

}
