package com.sparta.bapzip.review.domain.repository;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;

import java.util.UUID;

/**
 * 리뷰 엔티티에 대한 도메인 레벨의 저장소 인터페이스입니다.
 * 데이터베이스 접근에 대한 추상화를 제공하며, 구현체는 구체적인 DB 접근 방식을 처리합니다.
 */
public interface ReviewRepository {

    /**
     * 새로운 리뷰를 저장합니다.
     *
     * @param review 저장할 {@link ReviewEntity} 객체
     * @return 저장된 {@link ReviewEntity} 객체
     */
    ReviewEntity save(ReviewEntity review);

    /**
     * 특정 주문과 사용자에 대해 리뷰가 이미 존재하는지 확인합니다.
     *
     * @param orderId 확인할 주문 ID
     * @param userId  확인할 사용자 ID
     * @return 리뷰가 존재하면 {@code true}, 존재하지 않으면 {@code false}
     */
    boolean existsByOrderIdAndUserId(UUID orderId, Long userId);
}
