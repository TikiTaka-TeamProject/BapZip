package com.sparta.bapzip.ai.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class AiLogNotFound extends GlobalException {
    public AiLogNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
