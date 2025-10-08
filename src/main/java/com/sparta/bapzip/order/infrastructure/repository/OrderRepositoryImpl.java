package com.sparta.bapzip.order.infrastructure.repository;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {

    private OrderJpaRepository orderJpaRepository;
    @Override
    public Optional<OrderEntity> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }
}
