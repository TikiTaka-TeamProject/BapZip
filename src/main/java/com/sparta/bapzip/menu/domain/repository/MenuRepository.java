package com.sparta.bapzip.menu.domain.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {

    MenuEntity save(MenuEntity menu);

    /**
     * soft delete 처리 되지 않은 메뉴 조회 관련
     * + IsDeletedFalse
     */
    Optional<MenuEntity> findByIdAndIsDeletedFalse(UUID id);
    List<MenuEntity> findAllByShopIdAndIsDeletedFalse(UUID shopId);
    Page<MenuEntity> findByNameContainingAndIsDeletedFalse(String keyword, Pageable pageable);

    // 메뉴 List 반환 메서드 - order 도메인
    List<MenuEntity> findAllByIdIn(List<UUID> ids);

    /**
     * soft delete 처리 된 메뉴 포함 조회
     */
    List<MenuEntity> findAll();

}
