package com.sparta.bapzip.shop.domain.entity;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.exception.ShopAlreadyDeletedException;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShopEntityTest {

    private ShopEntity shop;
    private UserEntity owner;
    private CategoryEntity category;
    private Point point;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();
        point = geometryFactory.createPoint(new Coordinate(127.0, 37.0));

        owner = UserEntity.builder()
                .id(1L)
                .name("test_user")
                .build();

        category = CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Korean Food")
                .build();

        shop = ShopEntity.create(
                "테스트 가게",
                "서울시 강남구",
                owner,
                category,
                point
        );
    }

    @Test
    @DisplayName("이름 변경 테스트")
    void updateName() {
        shop.updateName("새로운 이름");
        assertThat(shop.getName()).isEqualTo("새로운 이름");
    }

    @Test
    @DisplayName("주소 변경 테스트")
    void updateAddress() {
        shop.updateAddress("서울시 송파구");
        assertThat(shop.getAddress()).isEqualTo("서울시 송파구");
    }

    @Test
    @DisplayName("위치(Point) 변경 테스트")
    void updateLocation() {
        Point newPoint = geometryFactory.createPoint(new Coordinate(128.0, 38.0));
        shop.updateLocation(newPoint);
        assertThat(shop.getLocation()).isEqualTo(newPoint);
    }

    @Test
    @DisplayName("카테고리 변경 테스트")
    void updateCategory() {
        CategoryEntity newCategory = CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("중식")
                .build();
        shop.updateCategory(newCategory);
        assertThat(shop.getCategory()).isEqualTo(newCategory);
    }

    @Test
    @DisplayName("상태 변경 테스트")
    void updateStatus() {
        shop.updateStatus(ShopStatusEnum.APPROVED);
        assertThat(shop.getStatus()).isEqualTo(ShopStatusEnum.APPROVED);
    }

    @Test
    @DisplayName("Soft Delete 테스트")
    void softDelete() {
        shop.softDelete(owner.getId());
        assertThat(shop.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("이미 삭제된 Shop softDelete 시 예외 발생")
    void softDelete_alreadyDeleted_throwsException() {
        shop.softDelete(owner.getId());
        assertThatThrownBy(() -> shop.softDelete(owner.getId()))
                .isInstanceOf(ShopAlreadyDeletedException.class);
    }

    @Test
    @DisplayName("정적 팩토리 메서드 create 테스트")
    void create() {
        ShopEntity created = ShopEntity.create(
                "새로운 가게",
                "서울시 관악구",
                owner,
                category,
                point
        );
        assertThat(created.getName()).isEqualTo("새로운 가게");
        assertThat(created.getAddress()).isEqualTo("서울시 관악구");
        assertThat(created.getOwner()).isEqualTo(owner);
        assertThat(created.getCategory()).isEqualTo(category);
        assertThat(created.getLocation()).isEqualTo(point);
        assertThat(created.getStatus()).isEqualTo(ShopStatusEnum.PENDING);
    }

    @Test
    @DisplayName("Builder 패턴 테스트")
    void builder() {
        ShopEntity shopEntity = ShopEntity.builder()
                .name("Builder 가게")
                .address("Builder 주소")
                .owner(owner)
                .category(category)
                .location(point)
                .status(ShopStatusEnum.APPROVED)
                .build();

        assertThat(shopEntity.getName()).isEqualTo("Builder 가게");
        assertThat(shopEntity.getStatus()).isEqualTo(ShopStatusEnum.APPROVED);
    }

    @Test
    @DisplayName("Builder에서 상태를 설정하지 않으면 기본값은 PENDING이다.")
    void builder_defaultStatus() {
        ShopEntity entity = ShopEntity.builder()
                .name("기본 상태 테스트")
                .address("서울시")
                .owner(owner)
                .category(category)
                .location(point)
                .build();

        assertThat(entity.getStatus()).isEqualTo(ShopStatusEnum.PENDING);
    }
}
