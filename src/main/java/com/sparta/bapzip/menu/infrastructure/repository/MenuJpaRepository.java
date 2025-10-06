package com.sparta.bapzip.menu.infrastructure.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, UUID> {

    List<MenuEntity> findAllByIdIn(List<UUID> ids);
}
