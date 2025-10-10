package com.sparta.bapzip.user.application.excpetion;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class PasswordNotMatchException extends GlobalException {

    public PasswordNotMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
