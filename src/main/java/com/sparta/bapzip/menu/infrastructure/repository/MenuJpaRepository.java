package com.sparta.bapzip.menu.infrastructure.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, UUID> {

    List<MenuEntity> findAllByIdIn(List<UUID> ids);

    @EntityGraph(attributePaths = {"shop"})
    Page<MenuEntity> findByNameContaining(String keyword, Pageable pageable);

    /**
     * 가게 ID를 기준으로 메뉴 조회
     * @param shopId(가게ID)
     * @return 해당 가게 메뉴 엔티티 리스트
     */
    List<MenuEntity> findAllByShopId(UUID shopId);
}
