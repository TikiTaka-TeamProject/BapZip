package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class CoordinateOutOfRangeException extends GlobalException {
    public CoordinateOutOfRangeException(ErrorCode errorCode)  {
        super(errorCode);
    }
}