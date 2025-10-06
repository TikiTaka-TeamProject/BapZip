package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.shop.presentation.dto.request.CreatShopRequest;
import com.sparta.bapzip.shop.presentation.dto.response.CreateShopResponse;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.util.List;

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

    /**
     * 가게 ID를 기준으로 ShopEntity 조회
     *
     * @param shopId 조회할 가게의 UUID
     * @return 조회된 ShopEntity
     * @throws GlobalException SHOP_NOT_FOUND 에러 발생 시
     */
    public ShopEntity getShopById(UUID shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new GlobalException(ErrorCode.SHOP_NOT_FOUND));
    }

    /**
     * 가게 ID 기준으로 상세 정보 조회
     * ShopEntity를 조회 후 필요한 정보를 ShopDetailResponse로 변환
     *
     * @param shopId 조회할 가게의 UUID
     * @return ShopDetailResponse 가게 상세 정보 DTO
     * @throws GlobalException SHOP_NOT_FOUND 에러 발생 시
     */
    public ShopDetailResponse getShopDetail(UUID shopId) {
        ShopEntity shop = getShopById(shopId);

        return ShopDetailResponse.from(shop);
    }

    public List<ShopEntity> getShopsByStatus(ShopStatusEnum shopStatusEnum) {
        if (shopStatusEnum == null) {
            return shopRepository.findAll();
        }
        return shopRepository.findByStatus(shopStatusEnum);
    }
}
