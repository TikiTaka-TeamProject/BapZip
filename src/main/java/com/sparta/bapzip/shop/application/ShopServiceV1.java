package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.kakaolocal.application.KakaoLocalServiceV1;
import com.sparta.bapzip.kakaolocal.application.dto.KakaoLocalResponseDto;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.shop.application.exception.*;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.application.dto.request.ShopCreationRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.user.application.UserServiceV1;
import lombok.RequiredArgsConstructor;
import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.application.ServiceAreaServiceV1;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.shop.application.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {

    private final ShopRepository shopRepository;
    private final UserServiceV1 userServiceV1;
    private final CategoryServiceV1 categoryServiceV1;
    private final ServiceAreaServiceV1 serviceAreaServiceV1;
    private final KakaoLocalServiceV1 kakaoLocalServiceV1;

    /**
     * 신규 가게 생성
     *
     * @param request   가게 생성 정보 DTO
     * @param user      user Entity
     * @return 생성된 가게 정보 DTO
     * @throws ShopAlreadyExistsException 이미 해당 Owner가 가게를 가지고 있는 경우
     * @throws GlobalException 좌표 범위 오류 발생 시
     */
    @Transactional
    public ShopEntity createShop(ShopCreationRequest request, UserEntity user) {

        // 1. Owner 조회
        UserEntity owner = userServiceV1.findUser(user.getId());

        // 2. 이미 Shop을 가진 Owner인지 체크
        if (shopRepository.existsByOwnerId(owner.getId())) {
            throw new ShopAlreadyExistsException(ErrorCode.SHOP_ALREADY_EXISTS);
        }

        // 3. 카테고리 조회
        CategoryEntity category = categoryServiceV1.getCategoryById(request.getCategoryId());

        // 4.주소료 좌표 조회
        KakaoLocalResponseDto kakaoData =kakaoLocalServiceV1.getResponse(request.getAddress());

        // 4. 좌표 유효성 체크
        double longitude = Double.parseDouble(kakaoData.getLongitude());
        double latitude = Double.parseDouble(kakaoData.getLatitude());
        validateCoordinates(longitude, latitude);

        // 6. 위치(Point) 생성
        Point location = createPoint(longitude, latitude);

        // 7. Shop 엔티티 생성
        ShopEntity shop = ShopEntity.create(
                request.getName(),
                request.getAddress(),
                owner,
                category,
                location
        );

        return shopRepository.save(shop);
    }

    /**
     * 좌표 유효성 검사
     *
     * @param longitude 경도
     * @param latitude  위도
     * @throws GlobalException 좌표가 유효 범위를 벗어날 경우
     */
    private void validateCoordinates(double longitude, double latitude) {
        if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
            throw new GlobalException(ErrorCode.COORDINATE_OUT_OF_RANGE);
        }
    }

    /**
     * Point 객체 생성
     *
     * @param longitude 경도
     * @param latitude  위도
     * @return 좌표 기반 Point 객체
     */
    private Point createPoint(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    /**
     * 가게 UUID 기준 Shop 조회
     *
     * @param shopId 조회할 가게 UUID
     * @return 조회된 ShopEntity
     * @throws ShopNotFoundException 존재하지 않는 가게 UUID일 경우
     */
    public ShopEntity getShopById(UUID shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException(ErrorCode.SHOP_NOT_FOUND));
    }

    /**
     * 가게 UUID 기준 Shop 상세 정보 조회
     *
     * @param shopId 조회할 가게 UUID
     * @return ShopDetailResponse 가게 상세 정보 DTO
     */
    public ShopDetailResponse getShopDetail(UUID shopId) {
        ShopEntity shop = getShopById(shopId);

        return ShopDetailResponse.from(shop);
    }

    /**
     * 요청자가 가게 소유자인지 검증
     *
     * @param shopId  검증할 가게 UUID
     * @param ownerId 요청자 ID
     * @throws GlobalException 요청자가 소유자가 아닌 경우
     */
    public void validateShopOwner(UUID shopId, Long ownerId) {
        ShopEntity shop = getShopById(shopId);

        // 권한 체크
        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new GlobalException(ErrorCode.UNAUTHORIZED_SHOP_ACCESS);
        }
    }

    /**
     * Shop 정보 수정
     * - 이름, 주소, 카테고리 수정 가능
     * - 주소 변경 시 좌표 및 서비스 지역 자동 갱신
     *
     * @param shopId           수정할 가게 UUID
     * @param user          요청자 ID (소유자 권한 체크)
     * @param shopUpdateRequest 수정 정보 DTO
     * @return 수정된 ShopDetailResponse
     */
    @Transactional
    public ShopDetailResponse updateShop(UUID shopId, UserEntity user, ShopUpdateRequest shopUpdateRequest) {
        // 1. shop 체크
        ShopEntity shop = getShopById(shopId);

        // 2. 권한 검증
        validateShopOwner(shopId, user.getId());

        // 3. 이름 수정
        if (shopUpdateRequest.getName() != null) {
            shop.updateName(shopUpdateRequest.getName());
        }

        // 4. 주소 수정 및 좌표 자동 갱신
        if (shopUpdateRequest.getAddress() != null) {
            shop.updateAddress(shopUpdateRequest.getAddress());

            // Kakao API로 주소 → 좌표 조회
            KakaoLocalResponseDto kakaoData = kakaoLocalServiceV1.getResponse(shopUpdateRequest.getAddress());
            double longitude = Double.parseDouble(kakaoData.getLongitude());
            double latitude = Double.parseDouble(kakaoData.getLatitude());
            validateCoordinates(longitude, latitude);

            // Point 생성
            Point newLocation = createPoint(longitude, latitude);
            shop.updateLocation(newLocation);
        }

        // 5. 카테고리 수정
        if (shopUpdateRequest.getCategoryId() != null) {
            CategoryEntity category = categoryServiceV1.getCategoryById(shopUpdateRequest.getCategoryId());
            shop.updateCategory(category);
        }

        return ShopDetailResponse.from(shop);
    }


    /**
     * 관리자용 Shop 상태 변경
     *
     * @param shopId   상태를 변경할 가게 UUID
     * @param newStatus 새로운 상태
     * @return 상태가 변경된 ShopDetailResponse
     */
    @Transactional
    public ShopDetailResponse updateShopStatus(UUID shopId, ShopStatusEnum newStatus) {
        ShopEntity shop = getShopById(shopId);
        shop.updateStatus(newStatus);
        return ShopDetailResponse.from(shop);
    }

    /**
     * 승인(APPROVED) 상태 가게 리스트 조회
     *
     * @return List<ShopEntity> 승인된 가게 리스트
     */
    public Page<ShopEntity> getApprovedShops(Pageable pageable) {
        return shopRepository.findByStatus(ShopStatusEnum.APPROVED, pageable);
    }

    /**
     * 상태별 가게 리스트 조회
     *
     * @param shopStatusEnum 조회할 상태 (null이면 전체 조회)
     * @return List<ShopEntity> 조회된 가게 리스트
     */
    public Page<ShopEntity> getShopsByStatus(ShopStatusEnum shopStatusEnum, Pageable pageable) {
        if (shopStatusEnum == null) {
            return shopRepository.findAll(pageable);
        }
        return shopRepository.findByStatus(shopStatusEnum, pageable);
    }

    /**
     * 가게 삭제 처리 (Soft Delete)
     *
     * @param shopId  삭제할 가게 UUID
     * @param ownerId 삭제 요청자 ID (소유자 권한 체크)
     * @throws ShopNotFoundException 존재하지 않거나 이미 삭제된 경우
     * @throws GlobalException 요청자가 소유자가 아닌 경우
     */
    @Transactional
    public void deleteShop(UUID shopId, Long ownerId) {
        ShopEntity shop = shopRepository.findByIdAndIsDeletedFalse(shopId)
                .orElseThrow(() -> new ShopNotFoundException(ErrorCode.SHOP_NOT_FOUND));

        // 권한 체크
        validateShopOwner(shopId, ownerId);

        shop.softDelete(ownerId);
    }

    public Page<ShopEntity> searchShops(String name, UUID categoryId, Polygon areaPolygon, Pageable pageable) {
        if (areaPolygon != null) {
            return shopRepository.findShopsByFilters(name, categoryId, areaPolygon, pageable);
        } else {
            return shopRepository.findShopsWithoutPolygon(name, categoryId, pageable);
        }
    }

}
