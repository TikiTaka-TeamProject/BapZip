package com.sparta.bapzip.review.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

/**
 * 리뷰 작성 요청을 위한 DTO(Data Transfer Object) 클래스입니다.
 * 클라이언트로부터 리뷰 점수, 내용, 주문 및 매장 정보를 전달받습니다.
 */
@Getter
public class CreateReviewRequest {

    /**
     * 리뷰 점수
     * <p>
     * 필수 입력 값이며, 정수형으로 작성합니다.
     */
    @NotNull
    private Integer score;

    /**
     * 리뷰 내용
     * <p>
     * 필수 입력 값이며, 공백이 아닌 문자열이어야 합니다.
     */
    @NotBlank
    private String content;

    /**
     * 리뷰 대상 매장의 ID
     * <p>
     * 필수 입력 값이며, UUID 형식이어야 합니다.
     */
    @NotNull
    private UUID shopId;

    /**
     * 리뷰 대상 주문의 ID
     * <p>
     * 필수 입력 값이며, UUID 형식이어야 합니다.
     */
    @NotNull
    private UUID orderId;
}