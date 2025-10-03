package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopServiceV1Test {

    private ShopRepository shopRepository;
    private ShopServiceV1 shopService;

    // 테스트 실행 전마다 수행, ShopServiceV1에 Mock Repository 주입
    @BeforeEach
    void setUp() {
        shopRepository = Mockito.mock(ShopRepository.class);
        shopService = new ShopServiceV1(shopRepository);
    }

    // 정상 조회 테스트: Shop이 존재하면 해당 엔티티 반환
    @Test
    void getShopById_shouldReturnShop_whenShopExists() {
        // given: 임의의 Shop ID, ShopEntity 생성
        UUID shopId = UUID.randomUUID();
        ShopEntity shop = new ShopEntity();
        // Mock Repository가 Optional.of(shop)를 반환하도록 설정
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        // when: 서비스 호출
        ShopEntity result = shopService.getShopById(shopId);

        // then: 반환된 ShopEntity가 null이 아니고, 기대값과 동일한지 검증
        assertNotNull(result);
        assertEquals(shop, result);

        // Repository의 findById가 정확히 1회 호출되었는지 검증
        verify(shopRepository, times(1)).findById(shopId);
    }

    // 예외 테스트: Shop이 존재하지 않으면 GlobalException 발생
    @Test
    void getShopById_shouldThrowException_whenShopNotFound() {
        // given: 임의의 Shop ID
        UUID shopId = UUID.randomUUID();
        // Mock Repository가 Optional.empty() 반환하도록 설정
        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        // when & then: getShopById 호출 시 GlobalException 발생 여부 확인
        GlobalException exception = assertThrows(GlobalException.class,
                () -> shopService.getShopById(shopId));

        // 발생한 예외의 ErrorCode가 SHOP_NOT_FOUND인지 검증
        assertEquals(ErrorCode.SHOP_NOT_FOUND, exception.getErrorCode());

        // Repository의 findById가 정확히 1회 호출되었는지 검증
        verify(shopRepository, times(1)).findById(shopId);
    }
}