package com.sparta.bapzip.kakaolocal.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class KakaoLocalResponseNotFound extends GlobalException {
    public KakaoLocalResponseNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
