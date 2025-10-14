package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.shop.application.exception.*;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.request.CreateShopRequest;
import com.sparta.bapzip.user.application.UserServiceV1;
import lombok.RequiredArgsConstructor;
import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.application.ServiceAreaServiceV1;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
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
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {

    private final ShopRepository shopRepository;
    private final UserServiceV1 userServiceV1;
    private final CategoryServiceV1 categoryServiceV1;
    private final ServiceAreaServiceV1 serviceAreaServiceV1;

    public CreateShopResponse createShop(CreateShopRequest createShopRequest, Long ownerId) {
        // 1. Owner 조회
        UserEntity owner = userServiceV1.findUser(ownerId);

        // 2. 이미 Shop을 가진 Owner인지 체크
        if (shopRepository.existsByOwnerId(owner.getId())) {
            throw new ShopAlreadyExistsException(ErrorCode.SHOP_ALREADY_EXISTS);
        }

        // 3. 카테고리 조회
        CategoryEntity category = categoryServiceV1.getCategoryById(createShopRequest.getCategoryId());

        // 4. 좌표 유효성 체크
        double lon = createShopRequest.getLongitude();
        double lat = createShopRequest.getLatitude();
        validateCoordinates(lon, lat);

        // 5. Service Area 조회 (좌표 기반)
        ServiceAreaEntity serviceArea = serviceAreaServiceV1.getServiceAreaByPoint(lon, lat);

        // 6. 위치(Point) 생성
        Point location = createPoint(createShopRequest.getLongitude(), createShopRequest.getLatitude());

        // 7. Shop 엔티티 생성
        ShopEntity shop = ShopEntity.create(
                createShopRequest,
                owner,
                category,
                serviceArea,
                location
        );

        // 8.저장
        ShopEntity saved = shopRepository.save(shop);

        return CreateShopResponse.from(saved);
    }

    private void validateCoordinates(double longitude, double latitude) {
        if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
            throw new GlobalException(ErrorCode.COORDINATE_OUT_OF_RANGE);
        }
    }

    private Point createPoint(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
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
                .orElseThrow(() -> new ShopNotFoundException(ErrorCode.SHOP_NOT_FOUND));
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
        // 1. shop 체크
        ShopEntity shop = getShopById(shopId);

        // 2. 권한 검증
        validateShopOwner(shopId, ownerId);

        // 3. 이름, 주소 수정
        if (shopUpdateRequest.getName() != null) shop.updateName(shopUpdateRequest.getName());
        if (shopUpdateRequest.getAddress() != null) shop.updateAddress(shopUpdateRequest.getAddress());

        // 4. 좌표 수정
        if (shopUpdateRequest.getLongitude() != null && shopUpdateRequest.getLatitude() != null) {
            double lon = shopUpdateRequest.getLongitude();
            double lat = shopUpdateRequest.getLatitude();
            validateCoordinates(lon, lat);

            Point newLocation = createPoint(lon, lat);
            shop.updateLocation(newLocation);

            // ServiceArea 업데이트
            ServiceAreaEntity serviceArea = serviceAreaServiceV1.getServiceAreaByPoint(lon, lat);
            shop.updateServiceArea(serviceArea);
        }

        // 5. 카테고리 수정
        if (shopUpdateRequest.getCategoryId() != null) {
            CategoryEntity category = categoryServiceV1.getCategoryById(shopUpdateRequest.getCategoryId());
            shop.updateCategory(category);
        }

        return ShopDetailResponse.from(shop);
    }

    // Manager 전용 상태 변경
    @Transactional
    public ShopDetailResponse updateShopStatus(UUID shopId, ShopStatusEnum newStatus) {
        ShopEntity shop = getShopById(shopId);
        shop.updateStatus(newStatus);
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

    /**
     * 가게 삭제 처리 (Soft Delete)
     *
     * - 실제 DB에서 삭제하지 않고, isDeleted = true 처리
     * - 삭제 요청 시 가게 존재 여부 확인
     * - 요청한 사용자가 소유자가 아닌 경우 접근 권한 예외 발생
     * - 삭제 시 deletedBy, deletedAt를 기록
     *
     * @param shopId 삭제할 가게 ID
     * @param ownerId 삭제를 요청한 사용자 ID (가게 소유자)
     * @throws GlobalException
     *   - SHOP_NOT_FOUND: 존재하지 않거나 이미 삭제된 가게
     *   - SHOP_DELETE_FORBIDDEN: 요청한 사용자가 소유자가 아닌 경우
     */
    @Transactional
    public void deleteShop(UUID shopId, Long ownerId) {
        ShopEntity shop = shopRepository.findByIdAndIsDeletedFalse(shopId)
                .orElseThrow(() -> new ShopNotFoundException(ErrorCode.SHOP_NOT_FOUND));

        // 권한 체크
        validateShopOwner(shopId, ownerId);

        shop.softDelete(ownerId);
    }
}
