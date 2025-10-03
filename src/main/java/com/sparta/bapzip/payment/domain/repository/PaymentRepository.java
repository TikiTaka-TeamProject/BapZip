package com.sparta.bapzip.payment.domain.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

public interface PaymentRepository {
    Optional<PaymentEntity> findByOrderId(Long orderId);
    Optional<PaymentEntity> findByPaymentKey(String paymentKey);
    PaymentEntity save(PaymentEntity payment);
}
