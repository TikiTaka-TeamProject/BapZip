package com.sparta.bapzip.category.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class CategoryException extends GlobalException {

    public CategoryException(ErrorCode errorCode) {
        super(errorCode);
    }
}