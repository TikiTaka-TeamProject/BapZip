package com.sparta.bapzip.review.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class DuplicateReviewException extends GlobalException {
    public DuplicateReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
