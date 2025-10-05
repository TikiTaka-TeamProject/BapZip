package com.sparta.bapzip.menu.domain.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;

import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {

    MenuEntity save(MenuEntity menu);

    Optional<MenuEntity> findById(UUID id);

}
