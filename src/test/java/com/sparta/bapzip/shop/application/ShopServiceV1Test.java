package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.kakaolocal.application.KakaoLocalServiceV1;
import com.sparta.bapzip.kakaolocal.application.dto.KakaoLocalResponseDto;
import com.sparta.bapzip.shop.application.dto.request.ShopCreationRequest;
import com.sparta.bapzip.shop.application.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ShopServiceV1Test {

    @Mock
    private ShopRepository shopRepository;
    @Mock private UserServiceV1 userServiceV1;
    @Mock private CategoryServiceV1 categoryServiceV1;
    @Mock private KakaoLocalServiceV1 kakaoLocalServiceV1;

    @InjectMocks
    private ShopServiceV1 shopServiceV1;

    private UserEntity user;
    private CategoryEntity category;
    private ShopEntity shop;
    private UUID shopId;

    @BeforeEach
    void setUp() {
        UUID categoryId = UUID.randomUUID();
        shopId = UUID.randomUUID();

        user = UserEntity.builder()
                .id(1L)
                .build();

        category = CategoryEntity.builder()
                .id(categoryId)
                .build();

        shop = ShopEntity.builder()
                .id(shopId)
                .owner(user)
                .category(category)
                .name("Test Shop")
                .build();
    }

    @Test
    @DisplayName("샵 생성 성공")
    void createShop_success() {
        ShopCreationRequest request = ShopCreationRequest.builder()
                .name("Test Shop")
                .address("Seoul")
                .categoryId(category.getId())
                .build();

        KakaoLocalResponseDto kakaoResponse = new KakaoLocalResponseDto(
                "Some Address", "road", "127.0", "37.5", "123456789", "987654321"
        );

        when(userServiceV1.findUser(user.getId())).thenReturn(user);
        when(shopRepository.existsByOwnerId(user.getId())).thenReturn(false);
        when(categoryServiceV1.getCategoryById(category.getId())).thenReturn(category);
        when(kakaoLocalServiceV1.getResponse("Seoul")).thenReturn(kakaoResponse);
        when(shopRepository.save(any())).thenReturn(shop);

        ShopEntity result = shopServiceV1.createShop(request, user);

        assertThat(result.getName()).isEqualTo("Test Shop");
        verify(shopRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("샵 생성 실패 - 이미 존재하는 경우")
    void createShop_alreadyExists() {
        lenient().when(userServiceV1.findUser(user.getId())).thenReturn(user);

        ShopCreationRequest request = ShopCreationRequest.builder()
                .name("Shop")
                .address("Some Address")
                .categoryId(category.getId())
                .build();

        assertThrows(Exception.class, () -> shopServiceV1.createShop(request, user));
    }

    @Test
    @DisplayName("샵 조회 성공")
    void getShopById_found() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        ShopEntity result = shopServiceV1.getShopById(shopId);
        assertThat(result.getId()).isEqualTo(shopId);
    }

    @Test
    @DisplayName("샵 조회 실패 - 존재하지 않는 경우")
    void getShopById_notFound() {
        UUID unknownId = UUID.randomUUID();
        when(shopRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> shopServiceV1.getShopById(unknownId));
    }

    @Test
    @DisplayName("샵 소유자 검증 성공")
    void validateShopOwner_success() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        shopServiceV1.validateShopOwner(shopId, user.getId());
    }

    @Test
    @DisplayName("샵 소유자 검증 실패 - 권한 없음")
    void validateShopOwner_unauthorized() {
        UserEntity otherUser = UserEntity.builder().id(2L).build();
        ShopEntity otherShop = ShopEntity.builder().id(shopId).owner(otherUser).build();

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(otherShop));

        assertThrows(Exception.class, () -> shopServiceV1.validateShopOwner(shopId, user.getId()));
    }

    @Test
    @DisplayName("샵 정보 업데이트 - 이름과 주소 변경")
    void updateShop_nameAndAddress() {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .name("New Name")
                .address("New Address")
                .build();

        KakaoLocalResponseDto kakaoResponse = new KakaoLocalResponseDto(
                "Some Address", "road", "127.0", "37.5", "hCodeExample", "bCodeExample"
        );

        ShopEntity mockShop = mock(ShopEntity.class);
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(mockShop));
        when(mockShop.getOwner()).thenReturn(user);
        when(mockShop.getCategory()).thenReturn(category);
        when(kakaoLocalServiceV1.getResponse("New Address")).thenReturn(kakaoResponse);

        doNothing().when(mockShop).updateName(any());
        doNothing().when(mockShop).updateAddress(any());
        doNothing().when(mockShop).updateLocation(any(Point.class));
        doNothing().when(mockShop).updateCategory(any());

        shopServiceV1.updateShop(shopId, user, request);

        verify(mockShop).updateName("New Name");
        verify(mockShop).updateAddress("New Address");
        verify(mockShop).updateLocation(any(Point.class));
    }

    @Test
    @DisplayName("샵 삭제 성공")
    void deleteShop_success() {
        ShopEntity mockShop = mock(ShopEntity.class);

        when(shopRepository.findByIdAndIsDeletedFalse(shopId)).thenReturn(Optional.of(mockShop));
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(mockShop));
        when(mockShop.getOwner()).thenReturn(user);
        doNothing().when(mockShop).softDelete(user.getId());

        shopServiceV1.deleteShop(shopId, user.getId());

        verify(mockShop).softDelete(user.getId());
    }

    @Test
    @DisplayName("승인된 샵 페이징 조회")
    void getApprovedShops_paging() {
        Page<ShopEntity> page = new PageImpl<>(Collections.emptyList());
        when(shopRepository.findByStatus(eq(ShopStatusEnum.APPROVED), any(Pageable.class)))
                .thenReturn(page);

        Page<ShopEntity> result = shopServiceV1.getApprovedShops(1, 10, "name", true);
        assertThat(result.getContent()).isEmpty();
    }
}