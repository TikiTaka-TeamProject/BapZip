package com.sparta.bapzip.shop.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * CoordinateOutOfRangeException
 *
 * 가게(Shop)의 좌표(Point)가 허용 범위를 벗어났을 때 발생하는 예외 클래스입니다.
 * 예를 들어, 위도(latitude)나 경도(longitude)가 정상 범위를 초과하면 이 예외가 발생합니다.
 * GlobalException을 상속하며, ErrorCode를 통해 상세한 오류 정보를 전달합니다.
 */
public class CoordinateOutOfRangeException extends GlobalException {

    /**
     * 생성자
     *
     * @param errorCode 발생한 오류에 대한 코드 및 메시지를 담고 있는 ErrorCode
     */
    public CoordinateOutOfRangeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
