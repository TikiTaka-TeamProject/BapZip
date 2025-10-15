package com.sparta.bapzip.menu.domain.entity;

import com.sparta.bapzip.menu.application.dto.request.MenuCreateRequest;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import com.sparta.bapzip.menu.domain.exception.InvalidMenuStatusException;
import com.sparta.bapzip.menu.domain.exception.MenuAlreadyDeletedException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MenuEntityTest {

    private UserEntity owner;
    private ShopEntity shop;

    @BeforeEach
    void setUp() {
        owner = UserEntity.builder()
                .id(1L)
                .name("사장님")
                .email("owner@test.com")
                .build();

        shop = ShopEntity.builder()
                .id(UUID.randomUUID())
                .name("테스트 가게")
                .owner(owner)
                .build();
    }

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class CreateMenuTest {

        @Test
        @DisplayName("메뉴 생성")
        void createMenu_success() {

            // given
            MenuCreateRequest request = new MenuCreateRequest("비빔밥", "맛있는 한식", 15000, shop.getId());

            // when
            MenuEntity menu = MenuEntity.createMenu(request, shop);

            // then
            assertThat(menu.getName()).isEqualTo("비빔밥");
            assertThat(menu.getContent()).isEqualTo("맛있는 한식");
            assertThat(menu.getPrice()).isEqualTo(15000);
            assertThat(menu.getStatus()).isEqualTo(MenuStatus.AVAILABLE); // 기본 값 확인
            assertThat(menu.getShop()).isEqualTo(shop);
            assertThat(menu.getShop().getOwner()).isEqualTo(owner);
        }
    }


    /**
     * 메뉴 정보 수정
     */
    @Nested
    @DisplayName("메뉴 정보 수정 테스트(PATCH)")
    class UpdateMenuInfoTest {

        @Test
        @DisplayName("메뉴 정보 전체 수정")
        void updateFields() {

            // given
            MenuEntity menu = MenuEntity.builder()
                    .name("초기이름")
                    .content("초기내용")
                    .price(10000)
                    .shop(shop)
                    .build();

            // when
            menu.updateName("수정이름");
            menu.updateContent("수정내용");
            menu.updatePrice(5000);

            // then
            assertThat(menu.getName()).isEqualTo("수정이름");
            assertThat(menu.getContent()).isEqualTo("수정내용");
            assertThat(menu.getPrice()).isEqualTo(5000);
        }


        @Test
        @DisplayName("메뉴 이름만 수정")
        void updateName_only() {

            // given
            MenuEntity menu = MenuEntity.builder()
                    .name("초기이름")
                    .content("초기내용")
                    .price(10000)
                    .shop(shop)
                    .build();

            // when
            menu.updateName("수정이름");

            // then
            assertThat(menu.getName()).isEqualTo("수정이름"); // 이름만 수정
            assertThat(menu.getContent()).isEqualTo("초기내용");
            assertThat(menu.getPrice()).isEqualTo(10000);
        }

        @Test
        @DisplayName("메뉴 내용만 수정")
        void updateContent_only() {
            MenuEntity menu = MenuEntity.builder()
                    .name("초기이름")
                    .content("초기내용")
                    .price(10000)
                    .shop(shop)
                    .build();

            menu.updateContent("수정내용");

            assertThat(menu.getName()).isEqualTo("초기이름");
            assertThat(menu.getContent()).isEqualTo("수정내용");
            assertThat(menu.getPrice()).isEqualTo(10000);
        }

        @Test
        @DisplayName("메뉴 가격만 수정")
        void updatePrice_only() {
            MenuEntity menu = MenuEntity.builder()
                    .name("초기이름")
                    .content("초기내용")
                    .price(10000)
                    .shop(shop)
                    .build();

            menu.updatePrice(5000);

            assertThat(menu.getName()).isEqualTo("초기이름");
            assertThat(menu.getContent()).isEqualTo("초기내용");
            assertThat(menu.getPrice()).isEqualTo(5000);
        }
    }


    /**
     * 메뉴 상태(AVAILABLE, SOLD_OUT) 수정
     */
    @Nested
    @DisplayName("메뉴 상태 관련 테스트")
    class UpdateMenuStatusTests {

        private MenuEntity menu;

        @BeforeEach
        void initMenu() {
            menu = MenuEntity.builder()
                    .name("라면")
                    .content("매운 라면")
                    .price(5000)
                    .status(MenuStatus.AVAILABLE)
                    .shop(shop)
                    .build();
        }

        @Test
        @DisplayName("메뉴 상태 변경")
        void updateStatus() {
            menu.updateStatus(MenuStatus.SOLD_OUT);
            assertThat(menu.getStatus()).isEqualTo(MenuStatus.SOLD_OUT);
        }

        @Test
        @DisplayName("메뉴 상태 값 예외 발생")
        void invalidMenuStatus() {
            String invalidValue = "TEST"; // enum에 없는 값

            assertThatThrownBy(() -> MenuStatus.from(invalidValue))
                    .isInstanceOf(InvalidMenuStatusException.class);
        }
    }


    @Nested
    @DisplayName("메뉴 삭제 테스트")
    class DeleteMenuTests{

        @Test
        @DisplayName("메뉴 삭제 성공")
        void deleteMenu_success() {

            // given
            MenuEntity menu = MenuEntity.builder()
                    .name("김치찌개")
                    .content("칼칼함")
                    .price(7000)
                    .build();

            // when
            menu.deleteMenu(1L);

            // then
            assertThat(menu.getIsDeleted()).isTrue();
        }

        @Test
        @DisplayName("이미 삭제된 경우 예외 발생")
        void deleteMenu_alreadyDeleted() {

            // given
            MenuEntity menu = MenuEntity.builder()
                    .name("된장찌개")
                    .content("국물 진함")
                    .price(7000)
                    .build();

            menu.markDeleted(1L);

            // when & then
            assertThatThrownBy(() -> menu.deleteMenu(1L))
                    .isInstanceOf(MenuAlreadyDeletedException.class);
        }
    }
}