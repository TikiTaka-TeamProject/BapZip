package com.sparta.bapzip.shop.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class ShopAlreadyDeletedException extends GlobalException {
    public ShopAlreadyDeletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
