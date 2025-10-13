package com.sparta.bapzip.order.infrastructure.repository;

import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderEntity save(OrderEntity order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Page<OrderEntity> findOrderByUser(UserEntity user, Pageable pageable) {
        return orderJpaRepository.findByUser(user, pageable);
    }

    @Override
    public Page<OrderEntity> findOrderByShopId(UUID shopId, Pageable pageable) {
        return orderJpaRepository.findByShopId(shopId, pageable);
    }

    @Override
    public Optional<OrderEntity> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }
}