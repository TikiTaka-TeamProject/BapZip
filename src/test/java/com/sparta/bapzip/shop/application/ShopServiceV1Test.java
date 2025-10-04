package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShopServiceV1Test {

    private ShopRepository shopRepository;
    private ShopServiceV1 shopService;

    @BeforeEach
    void setUp() {
        shopRepository = Mockito.mock(ShopRepository.class);
        shopService = new ShopServiceV1(shopRepository);
    }

    // Owner 권한 Test
    // 권한 있음
    @Test
    void validateShopOwner_success() {
        UUID shopId = UUID.randomUUID();
        Long ownerId = 1L;

        UserEntity owner = UserEntity.builder()
                .id(ownerId)
                .build();


        ShopEntity shop = ShopEntity.builder()
                .id(shopId)
                .owner(owner)
                .build();

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        assertDoesNotThrow(() -> shopService.validateShopOwner(shopId, ownerId));
    }

    // 권한 없음
    @Test
    void validateShopOwner_fail() {
        UUID shopId = UUID.randomUUID();
        Long ownerId = 1L;
        Long wrongOwnerId = 2L;

        UserEntity owner = UserEntity.builder()
                .id(ownerId)
                .build();

        ShopEntity shop = ShopEntity.builder()
                .id(shopId)
                .owner(owner)
                .build();

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        assertThrows(GlobalException.class, () -> shopService.validateShopOwner(shopId, wrongOwnerId));
    }

}