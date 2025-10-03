package com.sparta.bapzip.payment.infrastructure.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentKey(String paymentKey);
}
