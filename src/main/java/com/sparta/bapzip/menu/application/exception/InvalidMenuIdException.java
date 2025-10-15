package com.sparta.bapzip.menu.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class InvalidMenuIdException extends GlobalException {

    public InvalidMenuIdException(ErrorCode errorCode){
        super(errorCode);
    }
}
