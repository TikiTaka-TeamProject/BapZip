package com.sparta.bapzip.kakaomap.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

public class KakaoMapResponseNotFound extends GlobalException {
    public KakaoMapResponseNotFound(ErrorCode errorCode) {
        super(errorCode);
    }
}
