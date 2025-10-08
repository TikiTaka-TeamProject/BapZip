package com.sparta.bapzip.order.infrastructure.repository;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    @Override
    public Optional<OrderEntity> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }
}
