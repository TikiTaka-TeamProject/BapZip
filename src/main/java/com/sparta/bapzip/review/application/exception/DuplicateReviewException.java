package com.sparta.bapzip.review.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * 동일 주문에 대해 사용자가 이미 작성한 리뷰가 존재할 경우 발생하는 예외 클래스입니다.
 */
public class DuplicateReviewException extends GlobalException {
    /**
     * 중복 리뷰 예외 생성자
     *
     * @param errorCode 발생할 에러 코드
     */
    public DuplicateReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
