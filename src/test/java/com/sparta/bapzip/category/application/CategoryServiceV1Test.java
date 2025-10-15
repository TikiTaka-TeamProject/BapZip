package com.sparta.bapzip.category.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryServiceV1 테스트")
class CategoryServiceV1Test {

    // Mock 객체 선언
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ShopRepository shopRepository;

    // 테스트 대상 서비스에 Mock 객체 주입
    @InjectMocks
    private CategoryServiceV1 categoryServiceV1;

    // 테스트 공통 변수
    private UserEntity adminUser;
    private UserEntity nonAdminUser;
    private CategoryEntity category;
    private ShopEntity activeShop;
    private ShopEntity deletedShop; // 비활성 가게 대신 삭제된 가게로 명명하여 역할 명확화

    // UUID 타입 정의
    private final UUID CATEGORY_ID = UUID.randomUUID();
    private final UUID ACTIVE_SHOP_ID = UUID.randomUUID();
    private final UUID DELETED_SHOP_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // UserEntity: id는 Long 타입
        adminUser = UserEntity.builder()
                .id(3L)
                .name("메니저")
                .role(UserRoleEnum.MANAGER)
                .build();

        nonAdminUser = UserEntity.builder()
                .id(1L)
                .name("일반유저")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        // CategoryEntity: id는 UUID 타입
        category = CategoryEntity.builder()
                .id(CATEGORY_ID)
                .name("한식")
                .content("한국 전통 음식")
                .build();
        category.markCreated(adminUser.getId());

        // ShopEntity: id는 UUID 타입
        activeShop = ShopEntity.builder()
                .id(ACTIVE_SHOP_ID)
                .name("활성 가게")
                .status(ShopStatusEnum.APPROVED)
                .build();
        activeShop.markCreated(adminUser.getId());

        deletedShop = ShopEntity.builder()
                .id(DELETED_SHOP_ID)
                .name("삭제된 가게")
                .status(ShopStatusEnum.PENDING)
                .build();
        deletedShop.markCreated(adminUser.getId());
        deletedShop.markDeleted(adminUser.getId());
    }

    // --- 카테고리 ID 기준 가게 목록 조회 테스트 ---
    @Nested
    @DisplayName("카테고리별 가게 목록 조회")
    class ReadShopListByCategoryTest {

        private final int PAGE = 1;
        private final int SIZE = 10;
        private final String SORT_BY = "name";
        private final boolean IS_ASC = false;

        @Test
        @DisplayName("CUSTOMER 권한: 삭제되지 않은 활성 가게만 조회된다")
        void readActiveShopsByCustomer() {
            // Given: 일반 사용자용 조회 (isDeleted = false)
            List<ShopEntity> activeShops = List.of(activeShop);
            Page<ShopEntity> activeShopPage = new PageImpl<>(activeShops);

            // Mocking: shopRepository.findByCategoryIdAndIsDeletedFalse(UUID, Pageable) 호출 시 activeShopPage 반환
            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(activeShopPage);

            // When
            Page<ShopDetailForUserResponse> resultPage = categoryServiceV1.getShopsByCategory(
                    CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC
            );

            // Then
            assertThat(resultPage).isNotNull();
            assertThat(resultPage.getContent()).hasSize(1);
            assertThat(resultPage.getContent().get(0).getName()).isEqualTo(activeShop.getName());
        }

        @Test
        @DisplayName("MANAGER 권한: 삭제된 가게를 포함한 모든 가게가 조회된다")
        void readAllShopsByManager() {
            // Given: 관리자용 조회 (isDeleted 무시)
            List<ShopEntity> allShops = Arrays.asList(activeShop, deletedShop);
            Page<ShopEntity> allShopPage = new PageImpl<>(allShops);

            // Mocking: shopRepository.findByCategoryId(UUID, Pageable) 호출 시 allShopPage 반환
            when(shopRepository.findByCategoryId(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(allShopPage);

            // When
            Page<ShopDetailForUserResponse> resultPage = categoryServiceV1.getAllShopsByCategory(CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC);

            // Then
            assertThat(resultPage).isNotNull();
            assertThat(resultPage.getContent()).hasSize(2);
            assertThat(resultPage.getContent())
                    .extracting(ShopDetailForUserResponse::getName)
                    .containsExactlyInAnyOrder(activeShop.getName(), deletedShop.getName());
        }

        @Test
        @DisplayName("CUSTOMER 권한: 조회 결과가 없을 경우 예외가 발생한다")
        void throwExceptionWhenNoShopFound() {
            // Given: 조회 결과가 없는 Page 객체
            Page<ShopEntity> emptyShopPage = Page.empty();

            // Mocking: findByCategoryIdAndIsDeletedFalse 호출 시 emptyShopPage 반환
            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(emptyShopPage);

            // When & Then
            assertThatThrownBy(() -> categoryServiceV1.getShopsByCategory(
                    CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC
            )).isInstanceOf(Exception.class)
                    .hasMessageContaining("카테고리에 해당하는 가게가 존재하지 않습니다.");
        }
    }
}