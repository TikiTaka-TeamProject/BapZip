package com.sparta.bapzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Shop: 매니저 shop status 변경 관련
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),

    // Shop: 수정, 삭제
    UNAUTHORIZED_SHOP_ACCESS(HttpStatus.FORBIDDEN, "해당 가게에 접근할 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
