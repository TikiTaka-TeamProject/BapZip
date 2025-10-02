package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.entity.ShopStatus;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.request.RequestDto;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceAreaRepository serviceAreaRepository;


    public ShopEntity createShop(@RequestBody RequestDto shopRequestDto) {
        UserEntity owner = userRepository.findById(shopRequestDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        CategoryEntity category = categoryRepository.findById(shopRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        ServiceAreaEntity serviceArea = serviceAreaRepository.findById(shopRequestDto.getServiceAreaId())
                .orElseThrow(() -> new IllegalArgumentException("Service area not found"));

        ShopEntity shop = new ShopEntity(
                shopRequestDto.getName(),
                shopRequestDto.getAddress(),
                shopRequestDto.getLocation(),
                owner,
                category,
                serviceArea
        );

        return shopRepository.save(shop);
    }
}
