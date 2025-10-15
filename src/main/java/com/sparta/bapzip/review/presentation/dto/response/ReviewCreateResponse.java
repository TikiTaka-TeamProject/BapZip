package com.sparta.bapzip.review.presentation.dto.response;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * 리뷰 생성 후 반환되는 응답 DTO
 * <p>
 * 클라이언트에게 리뷰 생성 결과를 전달하기 위해 사용됩니다.
 * 리뷰 ID, 평점, 내용, 관련 가게/사용자/주문 ID 정보를 포함합니다.
 * </p>
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class ReviewCreateResponse {
    /** 리뷰 고유 ID */
    private UUID id;

    /** 리뷰 평점 */
    private int score;

    /** 리뷰 내용 */
    private String content;

    /** 리뷰가 작성된 가게 ID */
    private UUID shopId;

    /** 리뷰를 작성한 사용자 ID */
    private Long userId;

    /** 리뷰와 연결된 주문 ID */
    private UUID orderId;

    /**
     * ReviewEntity를 기반으로 ReviewCreateResponse DTO를 생성
     *
     * @param review ReviewEntity 객체
     * @return ReviewCreateResponse 생성된 DTO
     */
    public static ReviewCreateResponse from(ReviewEntity review) {
        return new ReviewCreateResponse(
                review.getId(),
                review.getScore(),
                review.getContent(),
                review.getShop().getId(),
                review.getUser().getId(),
                review.getOrder().getId()
        );
    }
}