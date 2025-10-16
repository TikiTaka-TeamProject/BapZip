package com.sparta.bapzip.shop.domain.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * 이미 삭제된 가게에 대해 삭제 요청 시 발생하는 예외
 */
public class ShopAlreadyDeletedException extends GlobalException {

    /**
     * 예외 생성자
     *
     * @param errorCode 에러 코드
     */
    public ShopAlreadyDeletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
