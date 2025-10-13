package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotDeliveringException extends GlobalException {
    public OrderNotDeliveringException(ErrorCode errorCode) {
        super(errorCode);
    }
}
