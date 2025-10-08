package com.sparta.bapzip.order.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class MenuNotInShopException extends GlobalException {
    public MenuNotInShopException(ErrorCode errorCode) {
        super(errorCode);
    }
}
