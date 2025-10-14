package com.sparta.bapzip.servicearea.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class ServiceAreaNotFoundException extends GlobalException {

    public ServiceAreaNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
