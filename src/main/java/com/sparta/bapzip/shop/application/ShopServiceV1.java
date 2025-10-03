package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.request.CreatShopRequest;
import com.sparta.bapzip.shop.presentation.dto.response.CreateShopResponse;
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


    public CreateShopResponse createShop(CreatShopRequest createShopRequest) {
        // null 체크
        if (createShopRequest.getOwnerId() == null)
            throw new IllegalArgumentException("Owner ID must not be null");
        if (createShopRequest.getCategoryId() == null)
            throw new IllegalArgumentException("Category ID must not be null");
        if (createShopRequest.getServiceAreaId() == null)
            throw new IllegalArgumentException("Service Area ID must not be null");
        if (createShopRequest.getLongitude() == null)
            throw new IllegalArgumentException("x must not be null");
        if (createShopRequest.getLatitude() == null)
            throw new IllegalArgumentException("y must not be null");
        
        // Owner 조회
        UserEntity owner = userRepository.findById(createShopRequest.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        // Category 조회
        CategoryEntity category = categoryRepository.findById(createShopRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Service Area 조회
        ServiceAreaEntity serviceArea = serviceAreaRepository.findById(createShopRequest.getServiceAreaId())
                .orElseThrow(() -> new IllegalArgumentException("Service Area not found"));


        // 위치(Point) 생성
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(
                new Coordinate(
                        createShopRequest.getLongitude(), // longitude: x
                        createShopRequest.getLatitude()  // latitude: y
                )
        );

        ShopEntity shop = new ShopEntity(
                createShopRequest.getName(),
                createShopRequest.getAddress(),
                location,
                owner,
                category,
                serviceArea
        );

        shop.markCreated(owner.getId());
        shop.markUpdated(owner.getId());

        ShopEntity saved = shopRepository.save(shop);

        return CreateShopResponse.builder()
                .shopId(saved.getId())
                .categoryName(saved.getCategory().getName())
                .name(saved.getName())
                .serviceAreaName(saved.getServiceArea().getName())
                .address(saved.getAddress())
                .status(saved.getStatus())
                .build();
    }
}
