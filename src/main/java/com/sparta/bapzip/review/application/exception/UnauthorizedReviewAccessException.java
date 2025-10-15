package com.sparta.bapzip.review.application.exception;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;

/**
 * 리뷰 작성자가 아닌 사용자가 리뷰에 접근하려고 할 때 발생하는 커스텀 예외 클래스입니다.
 *
 * <p>
 * 리뷰 수정, 삭제 등 작성자 전용 작업 시 권한이 없는 사용자가 요청할 경우 사용됩니다.
 * {@link ErrorCode}를 기반으로 예외 메시지와 상태 코드를 제공합니다.
 * </p>
 */
public class UnauthorizedReviewAccessException extends GlobalException {

    /**
     * 생성자
     *
     * @param errorCode 발생할 에러 코드
     */
    public UnauthorizedReviewAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
