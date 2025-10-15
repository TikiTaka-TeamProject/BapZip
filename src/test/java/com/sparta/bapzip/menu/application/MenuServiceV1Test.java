package com.sparta.bapzip.menu.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.menu.application.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.application.dto.request.MenuStatusUpdateRequest;
import com.sparta.bapzip.menu.application.dto.request.MenuUpdateRequest;
import com.sparta.bapzip.menu.application.exception.MenuNotFoundException;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import com.sparta.bapzip.menu.domain.repository.MenuRepository;
import com.sparta.bapzip.menu.presentation.dto.response.MenuCreateResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuDetailResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuListByShopResponse;
import com.sparta.bapzip.menu.presentation.dto.response.MenuSearchResponse;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.application.exception.ShopNotFoundException;
import com.sparta.bapzip.shop.application.exception.UnauthorizedShopAccessException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuServiceV1 단위 테스트")
class MenuServiceV1Test {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ShopServiceV1 shopServiceV1;

    @InjectMocks
    private MenuServiceV1 menuServiceV1;

    // data
    private ShopEntity testShop;
    private MenuEntity testMenu;
    private Long ownerId;
    private UUID shopId;
    private UUID menuId;

    @BeforeEach
    void setUp() {
        // test data
        ownerId = 1L;
        shopId = UUID.randomUUID();
        menuId = UUID.randomUUID();

        UserEntity testUser = UserEntity.builder()
                .id(ownerId)
                .build();

        testShop = ShopEntity.builder()
                .id(shopId)
                .name("TestShop")
                .owner(testUser)
                .build();

        testMenu = MenuEntity.builder()
                .id(menuId)
                .shop(testShop)
                .name("테스트 메뉴")
                .price(10000)
                .content("맛있는 메뉴입니다.")
                .status(MenuStatus.AVAILABLE)
                .build();
    }

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class CreateMenuTests {

        @Test
        @DisplayName("성공: 메뉴 생성 성공")
        void createMenu_success() {

            // given
            MenuCreateRequest request = new MenuCreateRequest("새 메뉴 이름", "새 메뉴", 12000, shopId);
            given(shopServiceV1.getShopById(request.shopId())).willReturn(testShop);
            willDoNothing().given(shopServiceV1).validateShopOwner(testShop.getId(), ownerId);
            given(menuRepository.save(any(MenuEntity.class))).willReturn(testMenu);

            // when
            MenuCreateResponse response = menuServiceV1.createMenu(request, ownerId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(testMenu.getId());
            assertThat(response.name()).isEqualTo(testMenu.getName());
            assertThat(response.content()).isEqualTo(testMenu.getContent());
            assertThat(response.price()).isEqualTo(testMenu.getPrice());
            assertThat(response.shopId()).isEqualTo(testShop.getId());

        }

        @Test
        @DisplayName("실패: 가게 Owner 아님")
        void createMenu_fail_notOwner() {

            // given
            MenuCreateRequest request = new MenuCreateRequest("새 메뉴 이름", "새 메뉴", 12000, shopId);
            Long nonOwnerId = 2L;

            given(shopServiceV1.getShopById(request.shopId())).willReturn(testShop);

            willThrow(new UnauthorizedShopAccessException(ErrorCode.UNAUTHORIZED_SHOP_ACCESS))
                    .given(shopServiceV1).validateShopOwner(testShop.getId(), nonOwnerId);

            // when & then
            assertThrows(UnauthorizedShopAccessException.class, () -> {
                menuServiceV1.createMenu(request, nonOwnerId);
            });

            verify(menuRepository, never()).save(any(MenuEntity.class)); // 호출 non 검증
        }
    }


    @Nested
    @DisplayName("메뉴 상세 조회 테스트")
    class GetMenuDetailsTests {

        @Test
        @DisplayName("성공: 상세 조회 성공")
        void getMenuDetails_success() {

            // given
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));

            // when
            MenuDetailResponse response = menuServiceV1.getMenuDetail(menuId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(testMenu.getId());
            assertThat(response.name()).isEqualTo(testMenu.getName());
            assertThat(response.content()).isEqualTo(testMenu.getContent());
            assertThat(response.price()).isEqualTo(testMenu.getPrice());
            assertThat(response.shopId()).isEqualTo(testShop.getId());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 메뉴")
        void getMenuDetails_fail() {

            // given
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.empty());

            // when & then
            assertThrows(MenuNotFoundException.class, () -> {
                menuServiceV1.getMenuDetail(menuId);
            });
        }
    }


    @Nested
    @DisplayName("가게별 메뉴 목록 조회 테스트")
    class GetMenusByShopTests {

        @Test
        @DisplayName("성공: 조회 성공")
        void getMenusByShop_success() {

            // given
            given(shopServiceV1.getShopById(shopId)).willReturn(testShop);
            given(menuRepository.findAllByShopIdAndIsDeletedFalse(shopId)).willReturn(List.of(testMenu));

            // when
            MenuListByShopResponse response = menuServiceV1.getMenusByShop(shopId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.shopName()).isEqualTo(testShop.getName());
            assertThat(response.menus()).hasSize(1); // 1개
            assertThat(response.menus().get(0).name()).isEqualTo(testMenu.getName());
            assertThat(response.menus().get(0).price()).isEqualTo(testMenu.getPrice());
            assertThat(response.menus().get(0).id()).isEqualTo(testMenu.getId());
            assertThat(response.menus().get(0).status()).isEqualTo(testMenu.getStatus());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 가게")
        void getMenusByShop_fail_shopNotFound() {

            // given
            UUID nonExistShopId = UUID.randomUUID();
            given(shopServiceV1.getShopById(nonExistShopId)).willThrow(new ShopNotFoundException(ErrorCode.SHOP_NOT_FOUND));

            // when & then
            assertThrows(ShopNotFoundException.class, () -> {
                menuServiceV1.getMenusByShop(nonExistShopId);
            });
        }
    }


    @Nested
    @DisplayName("메뉴 이름 기반 검색(페이징) 테스트")
    class SearchMenusTests {

        @Test
        @DisplayName("성공: 메뉴 검색 완료")
        void searchMenus_success() {

            // given = default 값 + price
            String keyword = "테스트";
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "price"));
            Page<MenuEntity> mockedPage = new PageImpl<>(List.of(testMenu), pageable, 1);

            given(menuRepository.findByNameContainingAndIsDeletedFalse(eq(keyword), any(Pageable.class)))
                    .willReturn(mockedPage);

            // when
            Page<MenuSearchResponse> result = menuServiceV1.searchMenus(keyword, 1, 10, "price", false);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).name()).isEqualTo(testMenu.getName());

            verify(menuRepository).findByNameContainingAndIsDeletedFalse(eq(keyword), argThat(p ->
                    p.getPageNumber() == 0 &&
                            p.getPageSize() == 10 &&
                            p.getSort().getOrderFor("price").getDirection().isDescending()
            ));
        }

        @Test
        @DisplayName("성공: 유효하지 않은 size 값 입력 시 기본값(10)으로 조회")
        void searchMenus_withInvalidSize_shouldDefaultTo10() {

            // given
            String keyword = "테스트";
            int invalidSize = 25;

            Page<MenuEntity> mockedPage = new PageImpl<>(List.of(testMenu));
            given(menuRepository.findByNameContainingAndIsDeletedFalse(eq(keyword), any(Pageable.class)))
                    .willReturn(mockedPage);

            // when
            menuServiceV1.searchMenus(keyword, 1, invalidSize, "createdAt", false);

            // then -> 10 검증
            verify(menuRepository).findByNameContainingAndIsDeletedFalse(anyString(), argThat(p -> p.getPageSize() == 10));
        }
    }


    @Nested
    @DisplayName("메뉴 정보 수정 테스트(PATCH)")
    class UpdateMenuTests {

        @Test
        @DisplayName("성공: 모든 정보 수정")
        void updateMenu_success_fullUpdate() {

            MenuUpdateRequest request = new MenuUpdateRequest("새 이름", "새 내용", 5000);
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willDoNothing().given(shopServiceV1).validateShopOwner(testShop.getId(), ownerId);

            // when
            MenuDetailResponse response = menuServiceV1.updateMenu(menuId, request, ownerId);

            // then
            assertThat(response.name()).isEqualTo("새 이름");
            assertThat(response.content()).isEqualTo("새 내용");
            assertThat(response.price()).isEqualTo(5000);
        }

        @Test
        @DisplayName("성공: 부분 수정")
        void updateMenu_success_partialUpdate() {

            MenuStatus originalStatus = testMenu.getStatus();
            String originalContent = testMenu.getContent();
            Integer originalPrice = testMenu.getPrice();

            // 이름만 변경
            MenuUpdateRequest request = new MenuUpdateRequest("이름만 변경", null, null);
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willDoNothing().given(shopServiceV1).validateShopOwner(testShop.getId(), ownerId);

            // when
            MenuDetailResponse response = menuServiceV1.updateMenu(menuId, request, ownerId);

            // then
            assertThat(response.name()).isEqualTo("이름만 변경");
            assertThat(response.status()).isEqualTo(originalStatus);
            assertThat(response.content()).isEqualTo(originalContent);
            assertThat(response.price()).isEqualTo(originalPrice);
        }


        @Test
        @DisplayName("실패: 가게 Owner 아님")
        void updateMenu_fail_notOwner() {

            // given
            Long nonOwnerId = 2L;
            MenuUpdateRequest request = new MenuUpdateRequest("메뉴 이름 수정 시도",null, null);
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willThrow(new UnauthorizedShopAccessException(ErrorCode.UNAUTHORIZED_SHOP_ACCESS))
                    .given(shopServiceV1).validateShopOwner(testShop.getId(), nonOwnerId);

            // when & then
            assertThrows(UnauthorizedShopAccessException.class, () -> {
                menuServiceV1.updateMenu(menuId, request, nonOwnerId);
            });
        }
    }


    @Nested
    @DisplayName("메뉴 상태 수정 테스트")
    class UpdateMenuStatusTests {

        @Test
        @DisplayName("성공: 메뉴 상태 수정")
        void updateMenuStatus_success() {

            // given
            MenuStatusUpdateRequest request = new MenuStatusUpdateRequest(MenuStatus.SOLD_OUT.name());
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willDoNothing().given(shopServiceV1).validateShopOwner(testShop.getId(), ownerId);

            // when
            MenuDetailResponse response = menuServiceV1.updateMenuStatus(menuId, request, ownerId);

            // then
            assertThat(response.status()).isEqualTo(MenuStatus.SOLD_OUT);
        }

        @Test
        @DisplayName("실패: 가게 Owner 아님")
        void updateMenuStatus_fail_notOwner() {
            // given
            Long nonOwnerId = 2L;
            MenuStatusUpdateRequest  request = new MenuStatusUpdateRequest(MenuStatus.SOLD_OUT.name());
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willThrow(UnauthorizedShopAccessException.class)
                    .given(shopServiceV1).validateShopOwner(testShop.getId(), nonOwnerId);

            // when & then
            assertThrows(UnauthorizedShopAccessException.class, () -> {
                menuServiceV1.updateMenuStatus(menuId, request, nonOwnerId);
            });
        }
    }

    @Nested
    @DisplayName("메뉴 삭제(soft delete) 테스트")
    class DeleteMenuTests {

        @Test
        @DisplayName("성공: 메뉴 삭제 성공(soft delete)")
        void deleteMenu_success() {

            // given
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willDoNothing().given(shopServiceV1).validateShopOwner(testShop.getId(), ownerId);

            // when
            menuServiceV1.deleteMenu(menuId, ownerId);

            // then
            assertThat(testMenu.getIsDeleted()).isTrue();
        }


        @Test
        @DisplayName("실패: 존재하지 않는 메뉴")
        void deleteMenu_fail_menuNotFound() {

            // given
            UUID nonExistMenuId = UUID.randomUUID();
            given(menuRepository.findByIdAndIsDeletedFalse(nonExistMenuId)).willReturn(Optional.empty());

            // when & then
            assertThrows(MenuNotFoundException.class, () -> {
                menuServiceV1.deleteMenu(nonExistMenuId, ownerId);
            });
        }

        @Test
        @DisplayName("실패: 가게 Owner 아님")
        void deleteMenu_fail_notOwner() {

            // given
            Long nonOwnerId = 2L;
            given(menuRepository.findByIdAndIsDeletedFalse(menuId)).willReturn(Optional.of(testMenu));
            willThrow(new GlobalException(ErrorCode.UNAUTHORIZED_SHOP_ACCESS))
                    .given(shopServiceV1).validateShopOwner(testShop.getId(), nonOwnerId);

            // when & then
            assertThrows(GlobalException.class, () -> {
                menuServiceV1.deleteMenu(menuId, nonOwnerId);
            });
        }
    }

}
