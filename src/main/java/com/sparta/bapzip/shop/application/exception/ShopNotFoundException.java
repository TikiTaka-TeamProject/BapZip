package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class ShopNotFoundException extends GlobalException {
    public ShopNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
