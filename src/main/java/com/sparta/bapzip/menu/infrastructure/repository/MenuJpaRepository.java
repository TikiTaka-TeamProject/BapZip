package com.sparta.bapzip.menu.infrastructure.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, UUID> {
}
