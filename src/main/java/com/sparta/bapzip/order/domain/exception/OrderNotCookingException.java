package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotCookingException extends GlobalException {
    public OrderNotCookingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
