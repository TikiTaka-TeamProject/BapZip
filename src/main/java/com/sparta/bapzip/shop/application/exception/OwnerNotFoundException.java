package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class OwnerNotFoundException extends GlobalException {
    public OwnerNotFoundException(ErrorCode errorCode)  {
        super(errorCode);
    }
}