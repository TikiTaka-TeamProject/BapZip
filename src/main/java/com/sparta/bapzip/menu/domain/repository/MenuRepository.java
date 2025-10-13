package com.sparta.bapzip.menu.domain.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {

    MenuEntity save(MenuEntity menu);

    Optional<MenuEntity> findById(UUID id);

    List<MenuEntity> findAll();

    List<MenuEntity> findAllByShopId(UUID shopId);

    Page<MenuEntity> findByNameContaining(String keyword, Pageable pageable);

    List<MenuEntity> findAllByIdIn(List<UUID> ids);

}
