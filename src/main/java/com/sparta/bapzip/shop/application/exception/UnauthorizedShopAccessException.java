package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class UnauthorizedShopAccessException extends GlobalException {
    public UnauthorizedShopAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
