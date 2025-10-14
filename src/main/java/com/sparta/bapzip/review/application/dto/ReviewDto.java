package com.sparta.bapzip.review.application.dto;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 리뷰 조회용 DTO 클래스.
 * <p>
 * 프론트엔드에서 가게 리뷰 목록을 보여주기 위해 사용되며,
 * 리뷰 엔티티를 안전하게 전달하기 위한 데이터 전송 객체입니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class ReviewDto {

    /** 리뷰 ID */
    private UUID id;

    /** 리뷰 평점 */
    private int score;

    /** 리뷰 내용 */
    private String content;

    /** 리뷰 작성자 이름 */
    private String userName;

    /** 리뷰 작성일시 */
    private LocalDateTime createdAt;

    /**
     * ReviewEntity를 ReviewDto로 변환합니다.
     *
     * @param review 변환할 리뷰 엔티티
     * @return ReviewDto 객체
     */
    public static ReviewDto from(ReviewEntity review) {
        return ReviewDto.builder()
                .id(review.getId())
                .score(review.getScore())
                .content(review.getContent())
                .userName(review.getUser().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
