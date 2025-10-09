package com.sparta.bapzip.ordermenu.domain.repository;

import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;

import java.util.List;

public interface OrderMenuRepository {
    List<OrderMenuEntity> saveAll(List<OrderMenuEntity> orderMenuEntities);
}