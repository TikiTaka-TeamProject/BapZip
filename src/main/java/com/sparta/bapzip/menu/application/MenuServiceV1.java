package com.sparta.bapzip.menu.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import com.sparta.bapzip.menu.presentation.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuSearchRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuStatusUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.request.MenuUpdateRequest;
import com.sparta.bapzip.menu.presentation.dto.response.MenuCreateResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuDetailResponse;
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
     * 메뉴 이름 기반 조회 (search)
     * @param request keyword 검색할 메뉴 이름 -> 확장성을 위해 keyword라 명시
     * @param page,size,sort 페이징 및 정렬
     * @return 페이징 메뉴 DTO
     * validatedSize: page 10,30,50 size
     */
    public Page<MenuSearchResponse> searchMenus(MenuSearchRequest request, int page, int size, String sort) {

        // size 10, 30, 50 유효, 아닐 시 10 고정
        int validatedSize = List.of(10, 30, 50).contains(size) ? size : 10;

        // sort (정렬) param
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = "asc".equalsIgnoreCase(sortParams[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(direction, sortBy));

        Page<MenuEntity> menuPage = menuRepository.findByNameContaining(request.keyword(), pageable);
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
