package com.sparta.bapzip.menu.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import com.sparta.bapzip.menu.presentation.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.presentation.dto.response.MenuCreateResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuDetailResponse;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
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
    private final ShopServiceV1 shopServiceV1;

    // todo: 메서드 USER ROLE 검증 필요 -> 메뉴 생성, 삭제...

    // 메뉴 생성
    @Transactional
    public MenuCreateResponse createMenu(MenuCreateRequest request) {
        // 유효한 가게 검증 로직 shopService 호출
        ShopEntity shop = shopServiceV1.getShopById(request.shopId());

        MenuEntity menu = MenuEntity.createMenu(request, shop);
        MenuEntity savedMenu = menuRepository.save(menu);

        return MenuCreateResponse.from(savedMenu);
    }


    // 메뉴 상세 조회
    public MenuDetailResponse getMenuDetail(UUID menuId) {
        MenuEntity menu = getMenuById(menuId);
        return MenuDetailResponse.from(menu);
    }


    // --------------------- Entity 조회 메서드 -------------------------- //

    // 메뉴 엔티티 조회
    public MenuEntity getMenuById(UUID menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MENU_NOT_FOUND));
    }

    // 메뉴 List 반환
    // todo: 기본 조회 구현만 완료. 필요 시 추가 예외처리
    public List<MenuEntity> getMenusByIds(List<UUID> menuIds) {

        List<MenuEntity> menus = menuRepository.findAllByIdIn(menuIds);

        if (menus.size() != menuIds.size()) { // 요청 <-> db 조회 개수 다를 경우
            throw new GlobalException(ErrorCode.INVALID_MENU_ID);
        }

        return menus;
    }
}
