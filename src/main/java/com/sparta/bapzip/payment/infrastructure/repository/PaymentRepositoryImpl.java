package com.sparta.bapzip.payment.infrastructure.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import com.sparta.bapzip.payment.domain.repository.PaymentRepository;
import com.sparta.bapzip.review.domain.repository.ReviewRepository;

import java.util.Optional;

public class PaymentRepositoryImpl implements PaymentRepository {
    @Override
    public Optional<PaymentEntity> findById(Long paymentId) {
        return Optional.empty();
    }
}
