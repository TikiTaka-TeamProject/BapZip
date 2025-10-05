package com.sparta.bapzip.menu.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import com.sparta.bapzip.menu.presentation.dto.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceV1 {

    private final MenuRepository menuRepository;

    // todo: shop과 연결 필요 - 관련 도메인 코드는 따로 백업

    // 메뉴 상세 조회
    public MenuResponse getMenuById(UUID menuId) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MENU_NOT_FOUND));

        return MenuResponse.from(menu);
    }


    // --------------------- Entity 조회 메서드 -------------------------- //

    // 메뉴 List 반환 메서드
    // todo: 기본 조회 구현만 완료. 필요 시 추가 예외처리
    public List<MenuEntity> findMenusByIds(List<UUID> menuIds) {

        List<MenuEntity> menus = menuRepository.findAllByIdIn(menuIds);

        if (menus.size() != menuIds.size()) { // 요청 <-> db 조회 개수 다를 경우
            throw new GlobalException(ErrorCode.INVALID_MENU_ID);
        }

        return menus;
    }
}
