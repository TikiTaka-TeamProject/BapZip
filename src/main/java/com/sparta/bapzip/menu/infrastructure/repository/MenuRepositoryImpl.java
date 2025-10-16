package com.sparta.bapzip.menu.infrastructure.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    // 메뉴 생성
    @Override
    public MenuEntity save(MenuEntity menu) {
        return menuJpaRepository.save(menu);
    }

    // 메뉴 단일 조회
    @Override
    public Optional<MenuEntity> findByIdAndIsDeletedFalse(UUID id) {
        return menuJpaRepository.findByIdAndIsDeletedFalse(id);
    }

    // 메뉴 이름 기반 검색 조회
    @Override
    public Page<MenuEntity> findByNameContainingAndIsDeletedFalse(String keyword, Pageable pageable) {
        return menuJpaRepository.findByNameContainingAndIsDeletedFalse(keyword, pageable);
    }

    // 가게 ID로 모든 메뉴 조회
    @Override
    public List<MenuEntity> findAllByShopIdAndIsDeletedFalse(UUID shopId) {
        return menuJpaRepository.findAllByShopIdAndIsDeletedFalse(shopId);
    }

    // 메뉴 List 반환 메서드 - order 도메인
    @Override
    public List<MenuEntity> findAllByIdIn(List<UUID> ids) {
        return menuJpaRepository.findAllByIdIn(ids);
    }

    /**
     * soft delete 포함 조회 - 관리자용
     * 메뉴 전체 조회
     */
    @Override
    public Page<MenuEntity> findAll(Pageable pageable) {
        return menuJpaRepository.findAll(pageable);
    }
}
