package com.sparta.bapzip.payment.domain.repository;

import com.sparta.bapzip.payment.domain.entity.PaymentEntity;
import com.sparta.bapzip.payment.domain.entity.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Optional<PaymentEntity> findByOrderId(UUID orderId);
    PaymentEntity save(PaymentEntity payment);

}
