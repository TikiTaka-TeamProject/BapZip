package com.sparta.bapzip.category.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.exception.CategoryException;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryServiceV1 테스트")
class CategoryServiceV1Test {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private CategoryServiceV1 categoryServiceV1;

    private UserEntity adminUser;
    private UserEntity customerUser;
    private CategoryEntity category;
    private ServiceAreaEntity serviceArea;
    private ShopEntity activeShop;
    private ShopEntity deletedShop;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final UUID SERVICE_AREA_ID = UUID.randomUUID();
    private static final UUID ACTIVE_SHOP_ID = UUID.randomUUID();
    private static final UUID DELETED_SHOP_ID = UUID.randomUUID();

    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final String SORT_BY = "name";
    private static final boolean IS_ASC = false;

    @BeforeEach
    void setUp() {
        adminUser = UserEntityBuilder.builder()
                .id(3L)
                .name("매니저")
                .role(UserRoleEnum.MANAGER)
                .build();

        customerUser = UserEntityBuilder.builder()
                .id(1L)
                .name("일반유저")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        category = CategoryEntityBuilder.builder()
                .id(CATEGORY_ID)
                .name("한식")
                .content("한국 전통 음식")
                .createdBy(adminUser.getId())
                .build();

        serviceArea = ServiceAreaEntityBuilder.builder()
                .id(SERVICE_AREA_ID)
                .name("서울특별시 강남구")
                .createdBy(adminUser.getId())
                .build();

        activeShop = ShopEntityBuilder.builder()
                .id(ACTIVE_SHOP_ID)
                .name("활성 가게")
                .status(ShopStatusEnum.APPROVED)
                .category(category)
                .serviceArea(serviceArea)
                .owner(adminUser)
                .createdBy(adminUser.getId())
                .build();

        deletedShop = ShopEntityBuilder.builder()
                .id(DELETED_SHOP_ID)
                .name("삭제된 가게")
                .status(ShopStatusEnum.PENDING)
                .category(category)
                .serviceArea(serviceArea)
                .owner(adminUser)
                .createdBy(adminUser.getId())
                .isDeleted(true)
                .deletedBy(adminUser.getId())
                .build();
    }

    @Nested
    @DisplayName("카테고리별 가게 목록 조회")
    class ReadShopListByCategoryTest {

        @Test
        @DisplayName("일반 사용자는 삭제되지 않은 활성 가게만 조회할 수 있다")
        void getShopsByCategoryWithCustomerReturnsOnlyActiveShops() {
            // Given
            List<ShopEntity> activeShops = List.of(activeShop);
            Pageable expectedPageable = PageRequest.of(PAGE - 1, SIZE, Sort.by(Sort.Direction.DESC, SORT_BY));
            Page<ShopEntity> shopPage = new PageImpl<>(activeShops, expectedPageable, activeShops.size());

            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(shopPage);

            // When
            Page<ShopDetailForUserResponse> result = categoryServiceV1.getShopsByCategory(
                    CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("활성 가게");

            verify(shopRepository, times(1))
                    .findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class));
            verifyNoMoreInteractions(shopRepository);
        }

        @Test
        @DisplayName("관리자는 삭제된 가게를 포함한 모든 가게를 조회할 수 있다")
        void getAllShopsByCategoryWithManagerReturnsAllShops() {
            // Given
            List<ShopEntity> allShops = Arrays.asList(activeShop, deletedShop);
            Pageable expectedPageable = PageRequest.of(PAGE - 1, SIZE, Sort.by(Sort.Direction.DESC, SORT_BY));
            Page<ShopEntity> shopPage = new PageImpl<>(allShops, expectedPageable, allShops.size());

            when(shopRepository.findByCategoryId(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(shopPage);

            // When
            Page<ShopDetailForUserResponse> result = categoryServiceV1.getAllShopsByCategory(
                    CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent())
                    .extracting(ShopDetailForUserResponse::getName)
                    .containsExactlyInAnyOrder("활성 가게", "삭제된 가게");

            verify(shopRepository, times(1))
                    .findByCategoryId(eq(CATEGORY_ID), any(Pageable.class));
            verifyNoMoreInteractions(shopRepository);
        }

        @Test
        @DisplayName("조회 결과가 없을 경우 예외가 발생한다")
        void getShopsByCategoryWithNoResultsThrowsException() {
            // Given
            Page<ShopEntity> emptyPage = Page.empty();
            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(emptyPage);

            // When
            Throwable thrown = catchThrowable(() -> categoryServiceV1.getShopsByCategory(
                    CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC
            ));

            // Then
            assertThat(thrown)
                    .isInstanceOf(CategoryException.class);

            verify(shopRepository).findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class));
            verifyNoMoreInteractions(shopRepository);
        }

        @Test
        @DisplayName("정렬 조건이 올바르게 적용된다 - 오름차순")
        void getShopsByCategoryWithAscendingSortAppliesCorrectSort() {
            // Given
            List<ShopEntity> shops = List.of(activeShop);
            Pageable expectedPageable = PageRequest.of(PAGE - 1, SIZE, Sort.by(Sort.Direction.ASC, SORT_BY));
            Page<ShopEntity> shopPage = new PageImpl<>(shops, expectedPageable, shops.size());

            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(shopPage);

            // When
            categoryServiceV1.getShopsByCategory(CATEGORY_ID, PAGE, SIZE, SORT_BY, true);

            // Then
            verify(shopRepository).findByCategoryIdAndIsDeletedFalse(
                    eq(CATEGORY_ID),
                    argThat(pageable ->
                            pageable.getPageNumber() == PAGE - 1 &&
                                    pageable.getPageSize() == SIZE &&
                                    pageable.getSort().getOrderFor(SORT_BY) != null &&
                                    pageable.getSort().getOrderFor(SORT_BY).getDirection() == Sort.Direction.ASC
                    )
            );
        }

        @Test
        @DisplayName("페이징이 올바르게 적용된다")
        void getShopsByCategoryWithPaginationReturnsCorrectPage() {
            // Given
            int requestedPage = 2;
            int requestedSize = 5;
            List<ShopEntity> shops = List.of(activeShop);
            Pageable pageable = PageRequest.of(requestedPage - 1, requestedSize, Sort.by(Sort.Direction.DESC, SORT_BY));
            Page<ShopEntity> shopPage = new PageImpl<>(shops, pageable, 10);

            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(shopPage);

            // When
            Page<ShopDetailForUserResponse> result = categoryServiceV1.getShopsByCategory(
                    CATEGORY_ID, requestedPage, requestedSize, SORT_BY, IS_ASC
            );

            // Then
            assertThat(result.getNumber()).isEqualTo(requestedPage - 1);
            assertThat(result.getSize()).isEqualTo(requestedSize);
            assertThat(result.getTotalElements()).isEqualTo(10);
        }

        @Test
        @DisplayName("null 카테고리 ID로 조회 시 예외가 발생한다")
        void getShopsByCategoryWithNullCategoryIdThrowsException() {
            // When & Then
            assertThatThrownBy(() -> categoryServiceV1.getShopsByCategory(
                    null, PAGE, SIZE, SORT_BY, IS_ASC
            ))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("특정 조건의 가게만 조회")
        void getShopsByCategoryWithCustomConditionsUsingBuilder() {
            // Given - 빌더를 사용해 특정 조건의 가게 생성
            ShopEntity customShop = ShopEntityBuilder.builder()
                    .id(UUID.randomUUID())
                    .name("커스텀 가게")
                    .status(ShopStatusEnum.APPROVED)
                    .category(category)
                    .serviceArea(serviceArea)
                    .owner(customerUser)
                    .createdBy(customerUser.getId())
                    .build();

            List<ShopEntity> shops = List.of(customShop);
            Pageable pageable = PageRequest.of(PAGE - 1, SIZE, Sort.by(Sort.Direction.DESC, SORT_BY));
            Page<ShopEntity> shopPage = new PageImpl<>(shops, pageable, shops.size());

            when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(CATEGORY_ID), any(Pageable.class)))
                    .thenReturn(shopPage);

            // When
            Page<ShopDetailForUserResponse> result = categoryServiceV1.getShopsByCategory(
                    CATEGORY_ID, PAGE, SIZE, SORT_BY, IS_ASC
            );

            // Then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("커스텀 가게");
        }
    }

    // ========== Test Fixture Builders ==========

    /**
     * UserEntity 테스트 빌더
     */
    static class UserEntityBuilder {
        private Long id;
        private String name;
        private UserRoleEnum role;

        public static UserEntityBuilder builder() {
            return new UserEntityBuilder();
        }

        public UserEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserEntityBuilder role(UserRoleEnum role) {
            this.role = role;
            return this;
        }

        public UserEntity build() {
            return UserEntity.builder()
                    .id(id)
                    .name(name)
                    .role(role)
                    .build();
        }
    }

    /**
     * CategoryEntity 테스트 빌더
     */
    static class CategoryEntityBuilder {
        private UUID id = UUID.randomUUID();
        private String name = "기본 카테고리";
        private String content = "기본 설명";
        private Long createdBy = 1L;

        public static CategoryEntityBuilder builder() {
            return new CategoryEntityBuilder();
        }

        public CategoryEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CategoryEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryEntityBuilder content(String content) {
            this.content = content;
            return this;
        }

        public CategoryEntityBuilder createdBy(Long createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public CategoryEntity build() {
            CategoryEntity entity = CategoryEntity.builder()
                    .id(id)
                    .name(name)
                    .content(content)
                    .build();
            entity.markCreated(createdBy);
            return entity;
        }
    }

    /**
     * ServiceAreaEntity 테스트 빌더
     * Polygon은 테스트에서 null로 처리 (필수값이지만 실제 geometry 생성이 복잡하므로)
     */
    static class ServiceAreaEntityBuilder {
        private UUID id = UUID.randomUUID();
        private String name = "기본 서비스 지역";
        private Long createdBy = 1L;

        public static ServiceAreaEntityBuilder builder() {
            return new ServiceAreaEntityBuilder();
        }

        public ServiceAreaEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ServiceAreaEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ServiceAreaEntityBuilder createdBy(Long createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ServiceAreaEntity build() {
            ServiceAreaEntity entity = ServiceAreaEntity.builder()
                    .id(id)
                    .name(name)
                    .area(null) // 테스트에서는 geometry 생성 생략
                    .build();
            entity.markCreated(createdBy);
            return entity;
        }
    }

    /**
     * ShopEntity 테스트 빌더
     */
    static class ShopEntityBuilder {
        private UUID id = UUID.randomUUID();
        private String name = "기본 가게";
        private ShopStatusEnum status = ShopStatusEnum.APPROVED;
        private CategoryEntity category;
        private ServiceAreaEntity serviceArea;
        private UserEntity owner;
        private Long createdBy = 1L;
        private boolean isDeleted = false;
        private Long deletedBy;

        public static ShopEntityBuilder builder() {
            return new ShopEntityBuilder();
        }

        public ShopEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ShopEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ShopEntityBuilder status(ShopStatusEnum status) {
            this.status = status;
            return this;
        }

        public ShopEntityBuilder category(CategoryEntity category) {
            this.category = category;
            return this;
        }

        public ShopEntityBuilder serviceArea(ServiceAreaEntity serviceArea) {
            this.serviceArea = serviceArea;
            return this;
        }

        public ShopEntityBuilder owner(UserEntity owner) {
            this.owner = owner;
            return this;
        }

        public ShopEntityBuilder createdBy(Long createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ShopEntityBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public ShopEntityBuilder deletedBy(Long deletedBy) {
            this.deletedBy = deletedBy;
            return this;
        }

        public ShopEntity build() {
            ShopEntity entity = ShopEntity.builder()
                    .id(id)
                    .name(name)
                    .status(status)
                    .category(category)
                    .owner(owner)
                    .build();
            entity.markCreated(createdBy);
            if (isDeleted && deletedBy != null) {
                entity.markDeleted(deletedBy);
            }
            return entity;
        }
    }
}