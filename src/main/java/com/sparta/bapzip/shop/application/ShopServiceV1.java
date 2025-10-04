package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {
    private final ShopRepository shopRepository;

    /**
     * 승인 상태(APPROVED)인 가게 리스트 조회
     *
     * ShopRepository를 통해 상태가 APPROVED인 ShopEntity 리스트를 조회합니다.
     *
     * @return List<ShopEntity> 승인된 가게 리스트
     */
    public List<ShopEntity> getApprovedShops() {
        return shopRepository.findByStatus(ShopStatusEnum.APPROVED);
    }
}
