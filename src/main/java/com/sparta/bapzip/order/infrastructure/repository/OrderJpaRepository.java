package com.sparta.bapzip.order.infrastructure.repository;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(UUID orderId);
}
