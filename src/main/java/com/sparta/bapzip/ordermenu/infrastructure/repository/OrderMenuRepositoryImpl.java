package com.sparta.bapzip.ordermenu.infrastructure.repository;

import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.ordermenu.domain.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderMenuRepositoryImpl implements OrderMenuRepository {

    private final OrderMenuJpaRepository orderMenuJpaRepository;

    public List<OrderMenuEntity> saveAll(List<OrderMenuEntity> orderMenuEntities) {
        return orderMenuJpaRepository.saveAll(orderMenuEntities);
    }
}
