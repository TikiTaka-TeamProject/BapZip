package com.sparta.bapzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATE_USER_EXCEPTION(HttpStatus.CONFLICT, "중복 된 유저입니다."),
    UNAUTHORIZED_USER_EXCEPTION(HttpStatus.UNAUTHORIZED, "권한이 없는 유저입니다.");

    private final HttpStatus status;
    private final String message;
}
