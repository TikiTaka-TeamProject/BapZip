package com.sparta.bapzip.ordermenu.application;

import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.ordermenu.domain.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderMenuServiceV1 {
    private final OrderMenuRepository orderMenuRepository;

    public List<OrderMenuEntity> saveAll(List<OrderMenuEntity> orderMenuEntities) {
        return orderMenuRepository.saveAll(orderMenuEntities);
    }
}