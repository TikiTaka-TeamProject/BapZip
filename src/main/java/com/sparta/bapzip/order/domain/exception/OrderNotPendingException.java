package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OrderNotPendingException extends GlobalException {
    public OrderNotPendingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
