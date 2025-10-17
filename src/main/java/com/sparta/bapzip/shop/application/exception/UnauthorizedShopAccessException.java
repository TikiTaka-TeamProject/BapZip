package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * UnauthorizedShopAccessException
 *
 * 샵에 대한 권한이 없는 사용자가 접근할 때 발생하는 예외 클래스입니다.
 * GlobalException을 상속하며, ErrorCode를 통해 구체적인 오류 정보를 전달합니다.
 */
public class UnauthorizedShopAccessException extends GlobalException {

    /**
     * 생성자
     *
     * @param errorCode 발생한 오류에 대한 코드와 메시지를 담고 있는 ErrorCode
     */
    public UnauthorizedShopAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
