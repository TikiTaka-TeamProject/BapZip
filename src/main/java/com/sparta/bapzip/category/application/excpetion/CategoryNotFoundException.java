package com.sparta.bapzip.category.application.excpetion;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class CategoryNotFoundException extends GlobalException {
    
    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
