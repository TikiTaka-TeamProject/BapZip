package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class SoldOutMenuException extends GlobalException {
    public SoldOutMenuException(ErrorCode errorCode) {
        super(errorCode);
    }
}
