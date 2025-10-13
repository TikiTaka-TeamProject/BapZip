package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotAcceptedException extends GlobalException {
    public OrderNotAcceptedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
