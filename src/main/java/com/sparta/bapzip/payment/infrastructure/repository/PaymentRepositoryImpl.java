package com.sparta.bapzip.payment.infrastructure.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import com.sparta.bapzip.payment.domain.entity.PaymentStatusEnum;
import com.sparta.bapzip.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;
    @Override
    public Optional<PaymentEntity> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId);
    }
    @Override
    public PaymentEntity save(PaymentEntity payment) {
        return paymentJpaRepository.save(payment);
    }

}
