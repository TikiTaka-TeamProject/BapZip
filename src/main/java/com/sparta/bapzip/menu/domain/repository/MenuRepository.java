package com.sparta.bapzip.menu.domain.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {

    MenuEntity save(MenuEntity menu);

    Optional<MenuEntity> findById(UUID id);

    List<MenuEntity> findAllByIdIn(List<UUID> ids);

}
