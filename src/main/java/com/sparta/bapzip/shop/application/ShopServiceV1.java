package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.application.ServiceAreaServiceV1;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.shop.presentation.dto.request.CreatShopRequest;
import com.sparta.bapzip.shop.presentation.dto.response.CreateShopResponse;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.shop.presentation.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final CategoryServiceV1 categoryServiceV1;
    private final ServiceAreaServiceV1 serviceAreaServiceV1;


    public CreateShopResponse createShop(CreatShopRequest createShopRequest) {
        // Owner 조회
        UserEntity owner = userRepository.findById(createShopRequest.getOwnerId())
                .orElseThrow(() -> new GlobalException(ErrorCode.OWNER_NOT_FOUND));

        // 이미 Shop을 가진 Owner인지 체크
        if (shopRepository.existsByOwnerId(owner.getId())) {
            throw new GlobalException(ErrorCode.SHOP_ALREADY_EXISTS);
        }

        // Category 조회
        // TODO: Category 임시 메서드
        CategoryEntity category = categoryServiceV1.getCategoryById(createShopRequest.getCategoryId());

        // TODO: 임시 데이터
//        CategoryEntity category = CategoryEntity.builder()
//                .id(createShopRequest.getCategoryId())
//                .build();

        // Service Area 조회
        // TODO: ServiceArea 메서드 필요
        ServiceAreaEntity serviceArea = serviceAreaServiceV1.getServiceAreaById(createShopRequest.getServiceAreaId());

        // TODO: 임시 데이터
        // Service Area 더미 엔티티 생성
//        ServiceAreaEntity serviceArea = ServiceAreaEntity.builder()
//                .id(createShopRequest.getServiceAreaId())
//                .build();


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

    public void validateShopOwner(UUID shopId, Long ownerId) {
        ShopEntity shop = getShopById(shopId);

        // 권한 체크
        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new GlobalException(ErrorCode.UNAUTHORIZED_SHOP_ACCESS);
        }
    }

    /**
     * Shop 정보 수정
     * @param shopId 수정할 shop UUID
     * @param ownerId 요청한 사용자 ID (Owner 권한 체크)
     * @param shopUpdateRequest 수정할 정보
     * @return 수정된 Shop 정보를 ShopDetailResponse로 반환
     */
    @Transactional
    public ShopDetailResponse updateShop(UUID shopId, Long ownerId, ShopUpdateRequest shopUpdateRequest) {
        // shop 체크
        ShopEntity shop = getShopById(shopId);

        // 권한 검증 (Owner만 허용, 추후 Manager 권한 추가 가능)
        validateShopOwner(shopId, ownerId);

        // shop 정보 수정
        if (shopUpdateRequest.getName() != null) shop.updateName(shopUpdateRequest.getName());
        if (shopUpdateRequest.getAddress() != null) shop.updateAddress(shopUpdateRequest.getAddress());

        // TODO:메서드 따로 뺴내기
        // 좌표 수정 시 유효성 테크 후 Point 겍체 생성 ->
        if (shopUpdateRequest.getLongitude() != null && shopUpdateRequest.getLatitude() != null) {
            double lon = shopUpdateRequest.getLongitude();
            double lat = shopUpdateRequest.getLatitude();

            // 좌표 유효성 체크
            if (lon < -180 || lon > 180 || lat < -90 || lat > 90) {
                throw new GlobalException(ErrorCode.COORDINATE_OUT_OF_RANGE);
            }

            Point newLocation = new GeometryFactory().createPoint(new Coordinate(lon, lat));
            shop.updateLocation(newLocation);

            // TODO: 좌표 변경에 따라 serviceArea 자동 계산 및 업데이트
//            ServiceAreaEntity newServiceArea = serviceAreaRepository.findByLocation(lon, lat)
//                    .orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_AREA_NOT_FOUND));
//            shop.updateServiceArea(newServiceArea);

            ServiceAreaEntity serviceArea = serviceAreaServiceV1.getServiceAreaById(shopUpdateRequest.getServiceAreaId());
            shop.updateServiceArea(serviceArea);
        }

        // 카테고리 변경
        if (shopUpdateRequest.getCategoryId() != null) {
            CategoryEntity category = categoryServiceV1.getCategoryById(shopUpdateRequest.getCategoryId());
            shop.updateCategory(category);
        }

        return ShopDetailResponse.from(shop);
    }

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

    /**
     * 상태별 가게 목록 조회
     *
     * 전달된 상태(shopStatusEnum)에 따라 가게 목록을 조회합니다.
     * 상태가 null인 경우, 모든 가게를 조회합니다.
     *
     * @param shopStatusEnum 조회할 가게 상태 (ShopStatusEnum). null이면 전체 조회
     * @return List<ShopEntity> 조회된 가게 엔티티 리스트
     */
    public List<ShopEntity> getShopsByStatus(ShopStatusEnum shopStatusEnum) {
        if (shopStatusEnum == null) {
            return shopRepository.findAll();
        }
        return shopRepository.findByStatus(shopStatusEnum);
    }
}
