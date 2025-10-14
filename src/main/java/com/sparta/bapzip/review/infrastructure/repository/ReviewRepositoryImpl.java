package com.sparta.bapzip.review.infrastructure.repository;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import com.sparta.bapzip.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * {@link ReviewRepository} 인터페이스를 구현한 클래스입니다.
 * JPA를 사용하여 리뷰 엔티티의 저장 및 조회 기능을 제공합니다.
 */
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    /**
     * 리뷰 엔티티를 저장합니다.
     *
     * @param review 저장할 {@link ReviewEntity} 객체
     * @return 저장된 {@link ReviewEntity} 객체
     */
    @Override
    public ReviewEntity save(ReviewEntity review) {
        return reviewJpaRepository.save(review);
    }

    /**
     * 특정 주문과 사용자에 대해 리뷰가 이미 존재하는지 확인합니다.
     *
     * @param orderId 확인할 주문 ID
     * @param userId  확인할 사용자 ID
     * @return 리뷰가 존재하면 {@code true}, 존재하지 않으면 {@code false}
     */
    @Override
    public boolean existsByOrderIdAndUserId(UUID orderId, Long userId) {
        return reviewJpaRepository.existsByOrderIdAndUserId(orderId, userId);
    }
}