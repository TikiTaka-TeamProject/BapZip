package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.request.RequestDto;
import com.sparta.bapzip.shop.presentation.dto.response.ResponseDto;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceAreaRepository serviceAreaRepository;


    public ResponseDto createShop(RequestDto shopRequestDto) {
        // null 체크
        if (shopRequestDto.getOwnerId() == null)
            throw new IllegalArgumentException("Owner ID must not be null");
        if (shopRequestDto.getCategoryId() == null)
            throw new IllegalArgumentException("Category ID must not be null");
        if (shopRequestDto.getServiceAreaId() == null)
            throw new IllegalArgumentException("Service Area ID must not be null");
        if (shopRequestDto.getLongitude() == null)
            throw new IllegalArgumentException("x must not be null");
        if (shopRequestDto.getLatitude() == null)
            throw new IllegalArgumentException("y must not be null");
        
        // Owner 조회
        UserEntity owner = userRepository.findById(shopRequestDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        // Category 조회
        CategoryEntity category = categoryRepository.findById(shopRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Service Area 조회
        ServiceAreaEntity serviceArea = serviceAreaRepository.findById(shopRequestDto.getServiceAreaId())
                .orElseThrow(() -> new IllegalArgumentException("Service Area not found"));


        // 위치(Point) 생성
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(
                new Coordinate(
                        shopRequestDto.getLongitude(), // longitude: x
                        shopRequestDto.getLatitude()  // latitude: y
                )
        );

        ShopEntity shop = new ShopEntity(
                shopRequestDto.getName(),
                shopRequestDto.getAddress(),
                location,
                owner,
                category,
                serviceArea
        );

        shop.setCreatedBy(owner.getId());
        shop.setUpdatedBy(owner.getId());

        ShopEntity saved = shopRepository.save(shop);

        return new ResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getAddress(),
                saved.getStatus().name(),
                saved.getLocation().getX(),
                saved.getLocation().getY()
        );

    }
}
