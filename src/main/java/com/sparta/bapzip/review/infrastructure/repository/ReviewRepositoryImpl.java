package com.sparta.bapzip.review.infrastructure.repository;

import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import com.sparta.bapzip.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public ReviewEntity save(ReviewEntity review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public boolean existsByOrderIdAndUserId(UUID orderId, Long userId) {
        return reviewJpaRepository.existsByOrderIdAndUserId(orderId, userId);
    }
}
