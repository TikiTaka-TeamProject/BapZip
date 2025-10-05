package com.sparta.bapzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    // MENU todo: 상태코드 별 분리 필요
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 메뉴입니다."),
    INVALID_MENU_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 메뉴가 포함되어 있습니다.");

    private final HttpStatus status;
    private final String message;
}
