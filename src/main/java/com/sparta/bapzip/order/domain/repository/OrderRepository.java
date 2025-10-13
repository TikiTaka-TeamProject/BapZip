package com.sparta.bapzip.order.domain.repository;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<OrderEntity> findById(UUID orderId);
    OrderEntity save(OrderEntity order);
    Page<OrderEntity> findOrderByUser(UserEntity user, Pageable pageable);
    Page<OrderEntity> findOrderByShopId(UUID shopId, Pageable pageable);
}