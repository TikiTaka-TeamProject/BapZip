package com.sparta.bapzip.ai.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class AiLogNotFoundException extends GlobalException {
    public AiLogNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
