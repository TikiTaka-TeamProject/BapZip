package com.sparta.bapzip.menu.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import com.sparta.bapzip.menu.presentation.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuStatusUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.response.MenuCreateResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuDetailResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuListByShopResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuSearchResponse;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
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


    /**
     * 가게 별 메뉴 조회
     * @param shopId 조회할 가게 ID
     * @return 가게 정보와 메뉴 목록 포함 DTO
     */
    public MenuListByShopResponse getMenusByShop(UUID shopId) {

        // 유효한 가게 검증 로직 shopService 호출 (가게 정보 조회)
        ShopEntity shop = shopServiceV1.getShopById(shopId);

        // 해당 가게 모든 메뉴 조회
        List<MenuEntity> menus = menuRepository.findAllByShopId(shopId);

        // 메뉴 리스트 -> MenuItemDto 리스트로 변환
        List<MenuListByShopResponse.MenuItemDto> menuItems = menus.stream()
                .map(menu -> new MenuListByShopResponse.MenuItemDto(
                        menu.getId(),
                        menu.getName(),
                        menu.getContent(),
                        menu.getPrice(),
                        menu.getStatus()
                ))
                .toList();

        return new MenuListByShopResponse(shop.getId(), shop.getName(), menuItems);
    }


    // 메뉴 정보 수정
    // todo: (+) 의도한 공백 값 처리 로직 추가
    @Transactional
    public MenuDetailResponse updateMenu(UUID menuId, MenuUpdateRequest request) {

        MenuEntity menu = getMenuById(menuId);

        if (request.name() != null && !request.name().isBlank()) {
            menu.updateName(request.name());
        }
        if (request.content() != null && !request.content().isBlank()) {
            menu.updateContent(request.content());
        }
        if (request.price() != null) {
            menu.updatePrice(request.price());
        }

        return MenuDetailResponse.from(menu);
    }

    // 메뉴 상태 수정 - AVAILABLE, SOLD_OUT
    @Transactional
    public MenuDetailResponse updateMenuStatus(UUID menuId, MenuStatusUpdateRequest request){
        MenuEntity menu = getMenuById(menuId);
        menu.updateStatus(request.status());
        return MenuDetailResponse.from(menu);
    }


    /**
     * 메뉴 이름 기반 조회 (검색)
     *
     * @param keyword  검색어
     * @param page     페이지 번호 (0부터 시작)
     * @param size     한 페이지에 가져올 메뉴 개수 (유효 size: 10, 30, 50; 이외 값 입력시 10으로 고정)
     * @param sortBy   정렬할 필드명 (ex) "createdAt", "price")
     * @param isAsc    정렬 방향 (true: 오름차순, false: 내림차순(default))
     * @return         페이징된 메뉴 DTO Page<MenuSearchResponse>
     */
    public Page<MenuSearchResponse> searchMenus(String keyword, int page, int size, String sortBy, boolean isAsc) {

        // size 10, 30, 50 유효, 아닐 시 10 고정
        int validatedSize = List.of(10, 30, 50).contains(size) ? size : 10;

        // sort (정렬) param
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(direction, sortBy));

        Page<MenuEntity> menuPage = menuRepository.findByNameContaining(keyword, pageable);
        return menuPage.map(MenuSearchResponse::from);
    }



    // 메뉴 전체 조회
    public List<MenuSearchResponse> getAllMenus() {
        List<MenuEntity> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuSearchResponse::from)
                .toList(); // DTO -> List 반환
    }

    /**
     * 엔티티 조회 헬퍼
     */

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
