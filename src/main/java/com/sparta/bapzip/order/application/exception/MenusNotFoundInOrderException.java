package com.sparta.bapzip.order.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class MenusNotFoundInOrderException extends GlobalException {

    public MenusNotFoundInOrderException(ErrorCode errorCode) {
        super(errorCode);
    }
}
