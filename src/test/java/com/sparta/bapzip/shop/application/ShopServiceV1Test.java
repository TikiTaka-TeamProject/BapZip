package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.kakaolocal.application.KakaoLocalServiceV1;
import com.sparta.bapzip.kakaolocal.application.dto.KakaoLocalResponseDto;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.application.dto.request.ShopCreationRequest;
import com.sparta.bapzip.shop.application.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ShopServiceV1Test {

    @Mock private ShopRepository shopRepository;
    @Mock private UserServiceV1 userServiceV1;
    @Mock private CategoryServiceV1 categoryServiceV1;
    @Mock private KakaoLocalServiceV1 kakaoLocalServiceV1;

    @InjectMocks private ShopServiceV1 shopServiceV1;

    private UserEntity user;
    private CategoryEntity category;
    private ShopEntity shop;
    private UUID shopId;

    @BeforeEach
    void setUp() {
        shopId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        user = UserEntity.builder().id(1L).build();
        category = CategoryEntity.builder().id(categoryId).build();
        shop = ShopEntity.builder().id(shopId).owner(user).category(category).name("Test Shop").build();
    }

    /* ------------------- createShop ------------------- */
    @Test
    @DisplayName("샵 생성 성공")
    void createShop_success() {
        ShopCreationRequest request = ShopCreationRequest.builder()
                .name("Test Shop").address("Seoul").categoryId(category.getId()).build();
        KakaoLocalResponseDto kakaoResponse = new KakaoLocalResponseDto(
                "Addr", "road", "127.0", "37.5", "h", "b"
        );

        when(userServiceV1.findUser(user.getId())).thenReturn(user);
        when(shopRepository.existsByOwnerId(user.getId())).thenReturn(false);
        when(categoryServiceV1.getCategoryById(category.getId())).thenReturn(category);
        when(kakaoLocalServiceV1.getResponse("Seoul")).thenReturn(kakaoResponse);
        when(shopRepository.save(any())).thenReturn(shop);

        ShopEntity result = shopServiceV1.createShop(request, user);

        assertThat(result.getName()).isEqualTo("Test Shop");
    }

    @Test
    @DisplayName("샵 생성 실패 - 이미 존재")
    void createShop_alreadyExists() {
        when(userServiceV1.findUser(user.getId())).thenReturn(user);
        when(shopRepository.existsByOwnerId(user.getId())).thenReturn(true);

        ShopCreationRequest request = ShopCreationRequest.builder()
                .name("Shop").address("Addr").categoryId(category.getId()).build();

        assertThrows(Exception.class, () -> shopServiceV1.createShop(request, user));
    }

    /* ------------------- getShopById ------------------- */
    @Test
    @DisplayName("샵 조회 성공")
    void getShopById_found() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
        assertThat(shopServiceV1.getShopById(shopId).getId()).isEqualTo(shopId);
    }

    @Test
    @DisplayName("샵 조회 실패 - 존재하지 않음")
    void getShopById_notFound() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> shopServiceV1.getShopById(shopId));
    }

    /* ------------------- getShopDetail ------------------- */
    @Test
    @DisplayName("샵 상세 조회 성공")
    void getShopDetail_success() {
        when(shopRepository.findShopWithAvgScore(shopId)).thenReturn(Optional.of(shop));

        ShopDetailResponse response = shopServiceV1.getShopDetail(shopId);
        assertThat(response).isNotNull();
    }

    /* ------------------- validateShopOwner ------------------- */
    @Test
    @DisplayName("샵 소유자 검증 성공")
    void validateShopOwner_success() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
        shopServiceV1.validateShopOwner(shopId, user.getId());
    }

    @Test
    @DisplayName("샵 소유자 검증 실패")
    void validateShopOwner_unauthorized() {
        UserEntity other = UserEntity.builder().id(2L).build();
        ShopEntity otherShop = ShopEntity.builder().id(shopId).owner(other).build();
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(otherShop));

        assertThrows(Exception.class, () -> shopServiceV1.validateShopOwner(shopId, user.getId()));
    }

    /* ------------------- updateShop ------------------- */
    @Test
    @DisplayName("샵 업데이트 - 이름, 주소, 카테고리 변경")
    void updateShop_allFields() {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .name("New Name").address("New Addr").categoryId(category.getId()).build();

        ShopEntity mockShop = mock(ShopEntity.class);
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(mockShop));
        when(mockShop.getOwner()).thenReturn(user);
        when(kakaoLocalServiceV1.getResponse(any())).thenReturn(new KakaoLocalResponseDto("Addr", "road", "127", "37", "h", "b"));
        when(categoryServiceV1.getCategoryById(category.getId())).thenReturn(category);
        when(mockShop.getCategory()).thenReturn(category);

        doNothing().when(mockShop).updateName(any());
        doNothing().when(mockShop).updateAddress(any());
        doNothing().when(mockShop).updateLocation(any(Point.class));
        doNothing().when(mockShop).updateCategory(any());

        shopServiceV1.updateShop(shopId, user, request);

        verify(mockShop).updateName("New Name");
        verify(mockShop).updateAddress("New Addr");
        verify(mockShop).updateLocation(any(Point.class));
        verify(mockShop).updateCategory(category);
    }

    /* ------------------- updateShopStatus ------------------- */
    @Test
    @DisplayName("샵 상태 업데이트")
    void updateShopStatus_success() {
        ShopEntity mockShop = mock(ShopEntity.class);
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(mockShop));
        when(mockShop.getOwner()).thenReturn(user);
        when(mockShop.getCategory()).thenReturn(category); // 추가
        doNothing().when(mockShop).updateStatus(any());

        ShopDetailResponse response = shopServiceV1.updateShopStatus(shopId, ShopStatusEnum.APPROVED);

        assertThat(response).isNotNull();
        verify(mockShop).updateStatus(ShopStatusEnum.APPROVED);
    }

    /* ------------------- getApprovedShops ------------------- */
    @Test
    @DisplayName("승인된 샵 조회")
    void getApprovedShops_paging() {
        Page<ShopEntity> page = new PageImpl<>(Collections.singletonList(shop));
        when(shopRepository.findByStatus(eq(ShopStatusEnum.APPROVED), any(Pageable.class))).thenReturn(page);

        Page<ShopEntity> result = shopServiceV1.getApprovedShops(1, 10, "name", true);
        assertThat(result.getContent()).hasSize(1);
    }

    /* ------------------- getShopsByStatus ------------------- */
    @Test
    @DisplayName("상태별 샵 조회")
    void getShopsByStatus() {
        Page<ShopEntity> page = new PageImpl<>(Collections.singletonList(shop));
        when(shopRepository.findByStatus(eq(ShopStatusEnum.APPROVED), any(Pageable.class))).thenReturn(page);

        Page<ShopEntity> result = shopServiceV1.getShopsByStatus(ShopStatusEnum.APPROVED, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }

    /* ------------------- deleteShop ------------------- */
    @Test
    @DisplayName("샵 삭제 성공")
    void deleteShop_success() {
        ShopEntity mockShop = mock(ShopEntity.class);
        when(shopRepository.findByIdAndIsDeletedFalse(shopId)).thenReturn(Optional.of(mockShop));
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(mockShop)); // 추가
        when(mockShop.getOwner()).thenReturn(user);
        doNothing().when(mockShop).softDelete(user.getId());

        shopServiceV1.deleteShop(shopId, user.getId());
        verify(mockShop).softDelete(user.getId());
    }

    @Test
    @DisplayName("샵 삭제 실패 - 권한 없음")
    void deleteShop_unauthorized() {
        ShopEntity mockShop = mock(ShopEntity.class);
        UserEntity other = UserEntity.builder().id(2L).build();
        when(shopRepository.findByIdAndIsDeletedFalse(shopId)).thenReturn(Optional.of(mockShop));
        when(mockShop.getOwner()).thenReturn(other);

        assertThrows(Exception.class, () -> shopServiceV1.deleteShop(shopId, user.getId()));
    }

    /* ------------------- searchShops ------------------- */
    @Test
    @DisplayName("샵 검색 - 영역 포함")
    void searchShops_withPolygon() {
        Polygon polygon = mock(Polygon.class);
        Page<ShopEntity> page = new PageImpl<>(Collections.singletonList(shop));
        when(shopRepository.findShopsByPolygon(anyString(), any(), any(), any(Pageable.class))).thenReturn(page);

        Page<ShopEntity> result = shopServiceV1.searchShops("Test", null, polygon, 1, 10, "name", true);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("샵 검색 - 영역 미포함")
    void searchShops_withoutPolygon() {
        Page<ShopEntity> page = new PageImpl<>(Collections.singletonList(shop));
        when(shopRepository.findShops(anyString(), any(), any(Pageable.class))).thenReturn(page);

        Page<ShopEntity> result = shopServiceV1.searchShops("Test", null, null, 1, 10, "name", true);
        assertThat(result.getContent()).hasSize(1);
    }

}
