package com.sparta.bapzip.kakaolocal.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class KakaoLocalResponseNotFoundException extends GlobalException {
    public KakaoLocalResponseNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
