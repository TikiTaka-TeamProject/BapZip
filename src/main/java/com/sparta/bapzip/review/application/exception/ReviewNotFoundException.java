package com.sparta.bapzip.review.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * 리뷰를 찾을 수 없을 때 발생하는 커스텀 예외 클래스입니다.
 *
 * <p>
 * 존재하지 않는 리뷰 ID로 접근 시 사용됩니다.
 * {@link ErrorCode}를 기반으로 예외 메시지와 상태 코드를 제공합니다.
 * </p>
 */
public class ReviewNotFoundException extends GlobalException {

    /**
     * 생성자
     *
     * @param errorCode 발생할 에러 코드
     */
    public ReviewNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
