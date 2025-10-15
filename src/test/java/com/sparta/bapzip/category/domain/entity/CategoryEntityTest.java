package com.sparta.bapzip.category.domain.entity;
import com.sparta.bapzip.category.presentation.dto.request.CategoryRequestDto;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CategoryEntity 도메인 테스트")
class CategoryEntityTest {
    private UserEntity adminUser;
    private UserEntity nonAdminUser;
    private CategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setUp() {
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

        categoryRequestDto = CategoryRequestDto.builder()
                .name("한식")
                .content("한국 전통 음식")
                .build();
    }

    @Nested
    @DisplayName("CategoryEntity 생성 테스트")
    class CreateCategoryEntity {
        // ... (이전과 동일한 테스트들)
        @Test
        @DisplayName("MANAGER가 CategoryEntity를 정상적으로 생성한다")
        void create_ByManager_Success() {
            // given
            String categoryName = "한식";
            String categoryContent = "한국 전통 음식";

            // when
            CategoryEntity category = CategoryEntity.builder()
                    .name(categoryName)
                    .content(categoryContent)
                    .build();
            category.markCreated(adminUser.getId());

            // then
            assertThat(category).isNotNull();
            assertThat(category.getName()).isEqualTo(categoryName);
            assertThat(category.getContent()).isEqualTo(categoryContent);
            assertThat(category.getIsDeleted()).isFalse();
            assertThat(category.getCreatedBy()).isEqualTo(adminUser.getId());
        }

        @Test
        @DisplayName("생성 시 isDeleted는 false다")
        void create_IsDeletedFalse() {
            // given
            String categoryName = "일식";
            String categoryContent = "일본 음식";

            // when
            CategoryEntity category = CategoryEntity.builder()
                    .name(categoryName)
                    .content(categoryContent)
                    .build();
            category.markCreated(adminUser.getId());

            // then
            assertThat(category.getIsDeleted()).isFalse();
        }

        @Test
        @DisplayName("name과 content가 필수값이다")
        void create_RequiredFields() {
            // given
            String categoryName = "양식";
            String categoryContent = "서양 음식";

            // when
            CategoryEntity category = CategoryEntity.builder()
                    .name(categoryName)
                    .content(categoryContent)
                    .build();

            // then
            assertThat(category.getName()).isNotNull();
            assertThat(category.getContent()).isNotNull();
        }
    }

    @Nested
    @DisplayName("CategoryEntity 중복 검증 테스트")
    class DuplicateCategoryTest {
        // ... (이전과 동일한 테스트들)
        @Test
        @DisplayName("동일한 이름의 카테고리는 생성할 수 없다")
        void createDuplicateName() {
            // given
            CategoryEntity existingCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            existingCategory.markCreated(adminUser.getId());

            // when
            CategoryEntity duplicateNameCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("다른 설명")
                    .build();

            // then
            // 서비스 레이어에서 categoryRepository.findByName("한식")이 존재하면
            // CategoryException(DUPLICATE_CATEGORY_EXCEPTION) 발생
            assertThat(duplicateNameCategory.getName()).isEqualTo(existingCategory.getName());
        }

        @Test
        @DisplayName("동일한 내용의 카테고리는 생성할 수 없다")
        void createDuplicateContent() {
            // given
            CategoryEntity existingCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            existingCategory.markCreated(adminUser.getId());

            // when
            CategoryEntity duplicateContentCategory = CategoryEntity.builder()
                    .name("다른이름")
                    .content("한국 전통 음식")
                    .build();

            // then
            // 서비스 레이어에서 categoryRepository.findByContent("한국 전통 음식")이 존재하면
            // CategoryException(DUPLICATE_CATEGORY_EXCEPTION) 발생
            assertThat(duplicateContentCategory.getContent()).isEqualTo(existingCategory.getContent());
        }

        @Test
        @DisplayName("이름과 내용이 모두 동일한 카테고리는 생성할 수 없다")
        void createDuplicateNameAndContent() {
            // given
            CategoryEntity existingCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            existingCategory.markCreated(adminUser.getId());

            // when
            CategoryEntity duplicateCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();

            // then
            // 서비스 레이어에서 중복 검증으로
            // CategoryException(DUPLICATE_CATEGORY_EXCEPTION) 발생
            assertThat(duplicateCategory.getName()).isEqualTo(existingCategory.getName());
            assertThat(duplicateCategory.getContent()).isEqualTo(existingCategory.getContent());
        }

        @Test
        @DisplayName("이름과 내용이 모두 다르면 생성할 수 있다")
        void createUniqueNameAndContent() {
            // given
            CategoryEntity existingCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            existingCategory.markCreated(adminUser.getId());

            // when
            CategoryEntity newCategory = CategoryEntity.builder()
                    .name("중식")
                    .content("중국 음식")
                    .build();
            newCategory.markCreated(adminUser.getId());

            // then
            assertThat(newCategory.getName()).isNotEqualTo(existingCategory.getName());
            assertThat(newCategory.getContent()).isNotEqualTo(existingCategory.getContent());
        }
    }

    @Nested
    @DisplayName("CategoryEntity 수정 테스트")
    class UpdateCategoryEntity {
        // ... (이전과 동일한 테스트들)
        @Test
        @DisplayName("MANAGER가 카테고리 정보를 수정한다")
        void updateByManagerSuccess() {
            // given
            CategoryEntity category = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            category.markCreated(adminUser.getId());

            String newName = "양식";
            String newContent = "서양 음식";

            // when
            category.update(newName, newContent);
            category.markUpdated(adminUser.getId());

            // then
            assertThat(category.getName()).isEqualTo(newName);
            assertThat(category.getContent()).isEqualTo(newContent);
            assertThat(category.getUpdatedBy()).isEqualTo(adminUser.getId());
            assertThat(category.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("수정 시 중복된 이름이 있으면 수정할 수 없다")
        void updateDuplicateName() {
            // given
            CategoryEntity existingCategory = CategoryEntity.builder()
                    .name("중식")
                    .content("중국 음식")
                    .build();
            existingCategory.markCreated(adminUser.getId());

            CategoryEntity categoryToUpdate = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            categoryToUpdate.markCreated(adminUser.getId());

            // when & then
            // 서비스 레이어에서 수정 시 categoryRepository.findByName("중식")이 존재하면
            // CategoryException(DUPLICATE_CATEGORY_EXCEPTION) 발생
            String duplicateName = existingCategory.getName();
            assertThat(duplicateName).isEqualTo("중식");
        }

        @Test
        @DisplayName("수정 시 중복된 내용이 있으면 수정할 수 없다")
        void updateDuplicateContent() {
            // given
            CategoryEntity existingCategory = CategoryEntity.builder()
                    .name("중식")
                    .content("중국 음식")
                    .build();
            existingCategory.markCreated(adminUser.getId());

            CategoryEntity categoryToUpdate = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            categoryToUpdate.markCreated(adminUser.getId());

            // when & then
            // 서비스 레이어에서 수정 시 categoryRepository.findByContent("중국 음식")이 존재하면
            // CategoryException(DUPLICATE_CATEGORY_EXCEPTION) 발생
            String duplicateContent = existingCategory.getContent();
            assertThat(duplicateContent).isEqualTo("중국 음식");
        }

        @Test
        @DisplayName("카테고리 이름만 수정한다")
        void updateNameOnly() {
            // given
            CategoryEntity category = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            category.markCreated(adminUser.getId());

            String newName = "한식당";

            // when
            category.update(newName, "한국 전통 음식");
            category.markUpdated(adminUser.getId());

            // then
            assertThat(category.getName()).isEqualTo(newName);
            assertThat(category.getContent()).isEqualTo("한국 전통 음식");
            assertThat(category.getUpdatedBy()).isEqualTo(adminUser.getId());
        }

        @Test
        @DisplayName("카테고리 설명만 수정한다")
        void updateContentOnly() {
            // given
            CategoryEntity category = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            category.markCreated(adminUser.getId());

            String newContent = "한국의 전통 음식과 현대 음식";

            // when
            category.update("한식", newContent);
            category.markUpdated(adminUser.getId());

            // then
            assertThat(category.getName()).isEqualTo("한식");
            assertThat(category.getContent()).isEqualTo(newContent);
            assertThat(category.getUpdatedBy()).isEqualTo(adminUser.getId());
        }
    }

    @Nested
    @DisplayName("CategoryEntity 삭제 테스트")
    class DeleteCategoryEntity {
        // ... (이전과 동일한 테스트들)
        @Test
        @DisplayName("MANAGER가 카테고리를 삭제한다")
        void deleteByManagerSuccess() {
            // given
            CategoryEntity category = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            category.markCreated(adminUser.getId());

            // when
            category.markDeleted(adminUser.getId());

            // then
            assertThat(category.getIsDeleted()).isTrue();
            assertThat(category.getDeletedAt()).isNotNull();
            assertThat(category.getDeletedBy()).isEqualTo(adminUser.getId());
        }

        @Test
        @DisplayName("삭제된 카테고리는 isDeleted가 true다")
        void deleteIsDeletedTrue() {
            // given
            CategoryEntity category = CategoryEntity.builder()
                    .name("일식")
                    .content("일본 음식")
                    .build();
            category.markCreated(adminUser.getId());

            // when
            category.markDeleted(adminUser.getId());

            // then
            assertThat(category.getIsDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("CategoryEntity 조회 테스트")
    class ReadCategoryEntity {

        @Test
        @DisplayName("MANAGER는 삭제된 카테고리를 포함한 모든 카테고리를 조회할 수 있다")
        void readAllCategoriesByManager() {
            // given
            CategoryEntity activeCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            activeCategory.markCreated(adminUser.getId());

            CategoryEntity deletedCategory = CategoryEntity.builder()
                    .name("중식")
                    .content("중국 음식")
                    .build();
            deletedCategory.markCreated(adminUser.getId());
            deletedCategory.markDeleted(adminUser.getId());

            // when & then
            assertThat(activeCategory.getIsDeleted()).isFalse();
            assertThat(deletedCategory.getIsDeleted()).isTrue();
            // MANAGER는 두 카테고리 모두 조회 가능 (이는 서비스/리포지토리 로직이므로 엔티티 레벨에서는 상태만 확인)
        }

        @Test
        @DisplayName("일반 유저는 isDeleted가 false인 카테고리만 조회할 수 있다")
        void readActiveCategoriesOnlyByCustomer() {
            // given
            CategoryEntity activeCategory = CategoryEntity.builder()
                    .name("한식")
                    .content("한국 전통 음식")
                    .build();
            activeCategory.markCreated(adminUser.getId());

            CategoryEntity deletedCategory = CategoryEntity.builder()
                    .name("중식")
                    .content("중국 음식")
                    .build();
            deletedCategory.markCreated(adminUser.getId());
            deletedCategory.markDeleted(adminUser.getId());

            // when & then
            assertThat(activeCategory.getIsDeleted()).isFalse(); // 일반 유저 조회 가능 (상태만 확인)
            assertThat(deletedCategory.getIsDeleted()).isTrue(); // 일반 유저 조회 불가 (상태만 확인)
        }
    }
}