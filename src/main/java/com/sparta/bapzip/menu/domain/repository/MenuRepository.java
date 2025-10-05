package com.sparta.bapzip.menu.domain.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;

public interface MenuRepository {

    MenuEntity save(MenuEntity menu);
}
