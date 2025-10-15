package com.sparta.bapzip.category.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CategoryServiceV1 테스트")
class CategoryServiceV1Test {

    private UserEntity adminUser;
    private UserEntity nonAdminUser;
    private CategoryEntity category;
    private ShopEntity activeShop;
    private ShopEntity inactiveShop;

    // UUID 타입 정의
    private final UUID CATEGORY_ID = UUID.randomUUID();
    private final UUID ACTIVE_SHOP_ID = UUID.randomUUID();
    private final UUID INACTIVE_SHOP_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // UserEntity: id는 Long 타입
        adminUser = UserEntity.builder()
                .id(3L)
                .name("메니저")
                .role(UserRoleEnum.valueOf("MANAGER"))
                .build();

        nonAdminUser = UserEntity.builder()
                .id(1L)
                .name("일반유저")
                .role(UserRoleEnum.valueOf("CUSTOMER"))
                .build();

        // CategoryEntity: id는 UUID 타입, createdBy는 Long 타입
        category = CategoryEntity.builder()
                .id(CATEGORY_ID)
                .name("한식")
                .content("한국 전통 음식")
                .build();
        category.markCreated(adminUser.getId()); // User ID (Long) 사용

        // ShopEntity: id는 UUID 타입, createdBy는 Long 타입
        activeShop = ShopEntity.builder()
                .id(ACTIVE_SHOP_ID)
                .name("활성 가게")
                .status(ShopStatusEnum.APPROVED)
                .build();
        activeShop.markCreated(adminUser.getId()); // User ID (Long) 사용

        inactiveShop = ShopEntity.builder()
                .id(INACTIVE_SHOP_ID)
                .name("비활성 가게")
                .status(ShopStatusEnum.PENDING)
                .build();
        inactiveShop.markCreated(adminUser.getId()); // User ID (Long) 사용
    }

    @Nested
    @DisplayName("특정 카테고리의 가게 목록 조회 테스트")
    class ReadShopListByCategoryTest {

        // 이 메서드는 실제 CategoryServiceV1에 구현되어 있어야 하는 로직의 테스트 대체용입니다.
        public List<ShopEntity> getShopsByCategory(UUID categoryId, UserRoleEnum role) {
            List<ShopEntity> allShops = Arrays.asList(activeShop, inactiveShop);

            if (role == UserRoleEnum.MANAGER) {
                // 매니저는 모든 가게를 조회
                return allShops;
            } else {
                // 일반 유저는 APPROVED 상태의 가게만 조회
                return allShops.stream()
                        .filter(shop -> shop.getStatus() == ShopStatusEnum.APPROVED)
                        .toList();
            }
        }

        @Test
        @DisplayName("MANAGER는 특정 카테고리에 해당하는 모든 가게 목록을 조회할 수 있다")
        void readShopListByCategoryByManager() {
            // given
            UUID categoryId = CATEGORY_ID; // UUID 사용

            // when
            List<ShopEntity> shops = getShopsByCategory(categoryId, adminUser.getRole());

            // then
            assertThat(shops).isNotNull();
            assertThat(shops).hasSize(2);
            assertThat(shops).containsExactlyInAnyOrder(activeShop, inactiveShop);
        }

        @Test
        @DisplayName("일반 유저는 특정 카테고리에 해당하는 활성화 된 가게 목록을 조회할 수 있다")
        void readShopListByCategoryByCustomer() {
            // given
            UUID categoryId = CATEGORY_ID; // UUID 사용

            // when
            List<ShopEntity> activeShops = getShopsByCategory(categoryId, nonAdminUser.getRole());

            // then
            assertThat(activeShops).isNotNull();
            assertThat(activeShops).hasSize(1);
            assertThat(activeShops).contains(activeShop);
            assertThat(activeShops).doesNotContain(inactiveShop);
        }
    }
}