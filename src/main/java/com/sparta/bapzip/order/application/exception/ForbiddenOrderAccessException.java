package com.sparta.bapzip.order.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class ForbiddenOrderAccessException extends GlobalException {
    public ForbiddenOrderAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
