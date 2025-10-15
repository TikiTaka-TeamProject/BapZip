package com.sparta.bapzip.menu.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class MenuAlreadyDeletedException extends GlobalException {

    public MenuAlreadyDeletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
