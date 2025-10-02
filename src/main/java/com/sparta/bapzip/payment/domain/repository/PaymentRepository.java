package com.sparta.bapzip.payment.domain.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

public interface PaymentRepository {
    Optional<PaymentEntity> findById(Long paymentId);
}
