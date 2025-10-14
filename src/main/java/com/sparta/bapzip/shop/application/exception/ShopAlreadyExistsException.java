package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class ShopAlreadyExistsException  extends GlobalException {
    public ShopAlreadyExistsException(ErrorCode errorCode)  {
        super(errorCode);
    }
}