package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * OwnerNotFoundException
 *
 * 가게(Shop)의 소유자(User)를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 * 예를 들어, 샵 생성 또는 소유자 검증 과정에서 해당 ID를 가진 유저가 존재하지 않으면 이 예외가 발생합니다.
 * GlobalException을 상속하며, ErrorCode를 통해 상세한 오류 정보를 전달합니다.
 */
public class OwnerNotFoundException extends GlobalException {

    /**
     * 생성자
     *
     * @param errorCode 발생한 오류에 대한 코드 및 메시지를 담고 있는 ErrorCode
     */
    public OwnerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
