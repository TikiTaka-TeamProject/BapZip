package com.sparta.bapzip.menu.infrastructure.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, UUID> {

    /**
     * soft delete 처리 되지 않은 메뉴 조회 관련
     */
    Optional<MenuEntity> findByIdAndIsDeletedFalse(UUID id);

    List<MenuEntity> findAllByShopIdAndIsDeletedFalse(UUID shopId);

    @EntityGraph(attributePaths = {"shop"})
    Page<MenuEntity> findByNameContainingAndIsDeletedFalse(String keyword, Pageable pageable);


    // 메뉴 List 반환 메서드 - order 도메인
    List<MenuEntity> findAllByIdIn(List<UUID> ids);

}
