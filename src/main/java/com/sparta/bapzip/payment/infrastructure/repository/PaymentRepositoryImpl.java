package com.sparta.bapzip.payment.infrastructure.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import com.sparta.bapzip.payment.domain.repository.PaymentRepository;
import com.sparta.bapzip.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;
    @Override
    public Optional<PaymentEntity> findByOrderId(Long orderId) {
        return paymentJpaRepository.findById(orderId);
    }

    @Override
    public Optional<PaymentEntity> findByPaymentKey(String paymentKey) {
        return paymentJpaRepository.findByPaymentKey(paymentKey);
    }

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        return paymentJpaRepository.save(payment);
    }
}
