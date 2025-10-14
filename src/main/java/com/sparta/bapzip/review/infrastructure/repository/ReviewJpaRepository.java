package com.sparta.bapzip.review.infrastructure.repository;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, UUID> {
    boolean existsByOrderIdAndUserId(UUID orderId, Long userId);
}