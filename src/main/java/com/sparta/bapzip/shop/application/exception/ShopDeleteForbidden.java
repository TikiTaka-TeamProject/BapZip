package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class ShopDeleteForbidden extends GlobalException {
    public ShopDeleteForbidden(ErrorCode errorCode)  {
        super(errorCode);
    }
}