package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * ShopAlreadyExistsException
 *
 * 이미 해당 소유자가 존재하는 샵을 생성하려 할 때 발생하는 예외 클래스입니다.
 * 예를 들어, 한 유저가 이미 샵을 가지고 있는 상태에서 또 다른 샵을 생성하려고 하면 이 예외가 발생합니다.
 * GlobalException을 상속하며, ErrorCode를 통해 구체적인 오류 정보를 전달합니다.
 */
public class ShopAlreadyExistsException extends GlobalException {

    /**
     * 생성자
     *
     * @param errorCode 발생한 오류에 대한 코드와 메시지를 담고 있는 ErrorCode
     */
    public ShopAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
