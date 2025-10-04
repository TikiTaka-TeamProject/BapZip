package com.sparta.bapzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Payment:결제 정보 조회 관련
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."), PAYMENT_CANCELLATION_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "결제 취소 할 수 없습니다."), ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
