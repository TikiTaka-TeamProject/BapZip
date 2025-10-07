package com.sparta.bapzip.user.application.excpetion;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class DuplicateUserException extends GlobalException {

    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
