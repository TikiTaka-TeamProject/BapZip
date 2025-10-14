package com.sparta.bapzip.review.domain.repository;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ReviewRepository {
    ReviewEntity save(ReviewEntity review);
    boolean existsByOrderIdAndUserId(UUID orderId, Long userId);
}
