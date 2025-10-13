package com.sparta.bapzip.order.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotFoundException extends GlobalException {
    public OrderNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
