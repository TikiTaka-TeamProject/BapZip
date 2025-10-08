package com.sparta.bapzip.user.application.excpetion;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class UnauthorizedUserException extends GlobalException {

    public UnauthorizedUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
