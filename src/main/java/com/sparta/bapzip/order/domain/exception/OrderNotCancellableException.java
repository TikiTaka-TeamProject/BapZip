package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotCancellableException extends GlobalException {
    public OrderNotCancellableException(ErrorCode errorCode) {
        super(errorCode);
    }
}
