package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {
    private final ShopRepository shopRepository;

    public List<ShopEntity> getShopsByStatus(ShopStatusEnum shopStatusEnum) {
        return shopRepository.findByStatus(shopStatusEnum);
    }
}
