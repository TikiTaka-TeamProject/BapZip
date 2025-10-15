package com.sparta.bapzip.menu.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class InvalidMenuStatusException extends GlobalException {

    public InvalidMenuStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
