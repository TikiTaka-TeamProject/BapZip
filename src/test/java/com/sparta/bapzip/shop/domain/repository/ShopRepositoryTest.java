package com.sparta.bapzip.shop.domain.repository;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ShopRepositoryTest {

    @Mock
    private ShopRepository shopRepository;

    private ShopEntity shop;
    private UUID shopId;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        shopId = UUID.randomUUID();
        shop = ShopEntity.builder()
                .id(shopId)
                .name("테스트 가게")
                .status(ShopStatusEnum.APPROVED)
                .build();

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("가게 저장 테스트")
    void saveShop() {
        when(shopRepository.save(any(ShopEntity.class))).thenReturn(shop);

        ShopEntity result = shopRepository.save(shop);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(shopId);
        assertThat(result.getName()).isEqualTo("테스트 가게");
    }

    @Test
    @DisplayName("가게 ID로 조회 테스트")
    void findById() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        Optional<ShopEntity> result = shopRepository.findById(shopId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(shopId);
    }

    @Test
    @DisplayName("가게 상태로 조회 테스트")
    void findByStatus() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));
        when(shopRepository.findByStatus(ShopStatusEnum.APPROVED, pageRequest)).thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findByStatus(ShopStatusEnum.APPROVED, pageRequest);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(ShopStatusEnum.APPROVED);
    }

    @Test
    @DisplayName("가게 삭제되지 않은 ID로 조회")
    void findByIdAndIsDeletedFalse() {
        when(shopRepository.findByIdAndIsDeletedFalse(shopId)).thenReturn(Optional.of(shop));

        Optional<ShopEntity> result = shopRepository.findByIdAndIsDeletedFalse(shopId);

        assertThat(result).isPresent();
        assertThat(result.get().getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("이름, 카테고리, Polygon으로 가게 조회")
    void findShopsByPolygon() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));

        String name = "테스트";
        UUID categoryId = UUID.randomUUID();
        Polygon polygon = null;

        when(shopRepository.findShopsByPolygon(eq(name), eq(categoryId), eq(polygon), eq(pageRequest)))
                .thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findShopsByPolygon(name, categoryId, polygon, pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("테스트 가게");
    }

    @Test
    @DisplayName("소유자 ID로 존재 여부 조회")
    void existsByOwnerId() {
        when(shopRepository.existsByOwnerId(1L)).thenReturn(true);

        boolean result = shopRepository.existsByOwnerId(1L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("카테고리 ID와 isDeleted false로 조회")
    void findByCategoryIdAndIsDeletedFalse() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));
        UUID categoryId = UUID.randomUUID();

        when(shopRepository.findByCategoryIdAndIsDeletedFalse(eq(categoryId), eq(pageRequest)))
                .thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageRequest);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(shopId);
    }

    @Test
    @DisplayName("모든 가게 조회")
    void findAll() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));
        when(shopRepository.findAll(eq(pageRequest))).thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findAll(pageRequest);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("카테고리 ID로 조회")
    void findByCategoryId() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));
        UUID categoryId = UUID.randomUUID();

        when(shopRepository.findByCategoryId(eq(categoryId), eq(pageRequest))).thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findByCategoryId(categoryId, pageRequest);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("필터로 가게 조회")
    void findShopsByFilters() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));
        String name = "테스트";
        UUID categoryId = UUID.randomUUID();
        Polygon polygon = null;

        when(shopRepository.findShopsByFilters(eq(name), eq(categoryId), eq(polygon), eq(pageRequest)))
                .thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findShopsByFilters(name, categoryId, polygon, pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Polygon 없이 필터 조회")
    void findShopsWithoutPolygon() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));

        when(shopRepository.findShopsWithoutPolygon(anyString(), any(UUID.class), eq(pageRequest)))
                .thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findShopsWithoutPolygon("테스트", UUID.randomUUID(), pageRequest);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("평균 점수 포함 조회")
    void findShopWithAvgScore() {
        when(shopRepository.findShopWithAvgScore(shopId)).thenReturn(Optional.of(shop));

        Optional<ShopEntity> result = shopRepository.findShopWithAvgScore(shopId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(shopId);
    }

    @Test
    @DisplayName("일반 필터 조회")
    void findShops() {
        Page<ShopEntity> shopPage = new PageImpl<>(List.of(shop));

        when(shopRepository.findShops(anyString(), any(UUID.class), eq(pageRequest)))
                .thenReturn(shopPage);

        Page<ShopEntity> result = shopRepository.findShops("테스트", UUID.randomUUID(), pageRequest);

        assertThat(result.getContent()).hasSize(1);
    }


}
