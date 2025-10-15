package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotCompletedException extends GlobalException {
    public OrderNotCompletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}