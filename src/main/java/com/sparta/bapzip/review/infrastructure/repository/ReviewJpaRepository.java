package com.sparta.bapzip.review.infrastructure.repository;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * 리뷰 엔티티에 대한 JPA Repository 인터페이스입니다.
 * Spring Data JPA를 사용하여 데이터베이스 CRUD 및 커스텀 쿼리 기능을 제공합니다.
 */
public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, UUID> {

    /**
     * 특정 주문과 사용자에 대해 리뷰가 이미 존재하는지 확인합니다.
     *
     * @param orderId 확인할 주문 ID
     * @param userId  확인할 사용자 ID
     * @return 리뷰가 존재하면 {@code true}, 존재하지 않으면 {@code false}
     */
    boolean existsByOrderIdAndUserId(UUID orderId, Long userId);
}