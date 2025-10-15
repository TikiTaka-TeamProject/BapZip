package com.sparta.bapzip.menu.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class MenuNotFoundException extends GlobalException {

    public MenuNotFoundException(ErrorCode errorCode){
        super(errorCode);
    }
}
