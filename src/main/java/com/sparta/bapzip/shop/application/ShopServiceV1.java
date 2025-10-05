package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
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

    // TODO: 타도메인간 통신 Service 개발 후 수정 필요
    private final UserRepository userRepository;
//    private final CategoryRepository categoryRepository;
//    private final ServiceAreaRepository serviceAreaRepository;


    public CreateShopResponse createShop(CreatShopRequest createShopRequest) {
        // Owner 조회
        UserEntity owner = userRepository.findById(createShopRequest.getOwnerId())
                .orElseThrow(() -> new GlobalException(ErrorCode.OWNER_NOT_FOUND));

        // 이미 Shop을 가진 Owner인지 체크
        if (shopRepository.existsByOwnerId(owner.getId())) {
            throw new GlobalException(ErrorCode.SHOP_ALREADY_EXISTS);
        }

        // Category 조회
        // TODO: Category 메서드 필요
//        CategoryEntity category = categoryRepository.findById(createShopRequest.getCategoryId())
//                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));

        // TODO: 임시 데이터
        CategoryEntity category = CategoryEntity.builder()
                .id(createShopRequest.getCategoryId())
                .build();

        // Service Area 조회
        // TODO: ServiceArea 메서드 필요
//        ServiceAreaEntity serviceArea = serviceAreaRepository.findById(createShopRequest.getServiceAreaId())
//                .orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_AREA_NOT_FOUND));

        // TODO: 임시 데이터
        // Service Area 더미 엔티티 생성
        ServiceAreaEntity serviceArea = ServiceAreaEntity.builder()
                .id(createShopRequest.getServiceAreaId())
                .build();


        // 위치(Point) 생성
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(
                new Coordinate(
                        createShopRequest.getLongitude(), // longitude: x
                        createShopRequest.getLatitude()  // latitude: y
                )
        );

        ShopEntity shop = ShopEntity.create(
                createShopRequest.getName(),
                createShopRequest.getAddress(),
                location,
                owner,
                category,
                serviceArea
        );

        // TODO: 생성, 수정 기록 메서드 필요
        // 변경 기록
        shop.markCreated(owner.getId());
        shop.markUpdated(owner.getId());

        // 저장
        ShopEntity saved = shopRepository.save(shop);

        return CreateShopResponse.from(saved);
    }
}
