package com.sparta.bapzip.user.application.excpetion;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class UserNotFoundException extends GlobalException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
