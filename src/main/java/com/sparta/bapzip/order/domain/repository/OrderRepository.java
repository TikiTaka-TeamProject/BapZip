package com.sparta.bapzip.order.domain.repository;

import com.sparta.bapzip.order.domain.entity.OrderEntity;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<OrderEntity> findById(UUID orderId);
    OrderEntity save(OrderEntity order);
}