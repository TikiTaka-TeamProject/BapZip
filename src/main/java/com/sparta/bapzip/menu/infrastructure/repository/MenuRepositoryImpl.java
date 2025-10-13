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
    public Optional<MenuEntity> findById(UUID id) {
        return menuJpaRepository.findById(id);
    }

    // 메뉴 이름 기반 검색 조회
    @Override
    public Page<MenuEntity> findByNameContaining(String keyword, Pageable pageable) {
        return menuJpaRepository.findByNameContaining(keyword, pageable);
    }

    // 메뉴 전체 조회 todo 내부용 구분
    @Override
    public List<MenuEntity> findAll(){
        return menuJpaRepository.findAll();
    }

    // 가게 ID로 모든 메뉴 조회
    @Override
    public List<MenuEntity> findAllByShopId(UUID shopId) {
        return menuJpaRepository.findAllByShopId(shopId);
    }

    // 메뉴 List 반환 메서드
    @Override
    public List<MenuEntity> findAllByIdIn(List<UUID> ids) {
        return menuJpaRepository.findAllByIdIn(ids);
    }
}
