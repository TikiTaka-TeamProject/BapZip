package com.sparta.bapzip.menu.infrastructure.repository;

import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    @Override
    public MenuEntity save(MenuEntity menu) {
        return menuJpaRepository.save(menu);
    }

    // 메뉴 단일 조회
    @Override
    public Optional<MenuEntity> findById(UUID id) {
        return menuJpaRepository.findById(id);
    }

    // 메뉴 List 반환 메서드
    @Override
    public List<MenuEntity> findAllByIdIn(List<UUID> ids) {
        return menuJpaRepository.findAllByIdIn(ids);
    }
}
