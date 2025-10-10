package com.sparta.bapzip.ordermenu.infrastructure.repository;

import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderMenuJpaRepository extends JpaRepository<OrderMenuEntity, UUID> {
}