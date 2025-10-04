package com.sparta.bapzip.shop.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.domain.repository.CategoryRepository;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.repository.ShopRepository;
import com.sparta.bapzip.shop.presentation.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopServiceV1 {

    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceAreaRepository serviceAreaRepository;

    public ShopEntity getShopById(UUID shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new GlobalException(ErrorCode.SHOP_NOT_FOUND));
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
        // 파라미터 체크
        if (shopId == null || ownerId == null || shopUpdateRequest == null) {
            throw new GlobalException(ErrorCode.MISSING_REQUIRED_PARAMETER);
        }

        // shop 체크
        ShopEntity shop = getShopById(shopId);

        // 권한 검증 (Owner만 허용, 추후 Manager 권한 추가 가능)
        validateShopOwner(shopId, ownerId);

        // shop 정보 수정
        if (shopUpdateRequest.getName() != null) shop.updateName(shopUpdateRequest.getName());
        if (shopUpdateRequest.getAddress() != null) shop.updateAddress(shopUpdateRequest.getAddress());
        // 좌표 수정 시 유효성 테크 후 Point 겍체 생성 ->
        // TODO:메서드 따로 뺴내기

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

            ServiceAreaEntity serviceArea = serviceAreaRepository.findById(UUID.fromString(shopUpdateRequest.getServiceAreaId()))
                    .orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_AREA_NOT_FOUND));
            shop.updateServiceArea(serviceArea);
        }

        // 카테고리 변경
        if (shopUpdateRequest.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(UUID.fromString(shopUpdateRequest.getCategoryId()))
                    .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
            shop.updateCategory(category);
        }


        // 저장
        ShopEntity updated = shopRepository.save(shop);

        return ShopDetailResponse.builder()
                .shopId(updated.getId())
                .name(updated.getName())
                .address(updated.getAddress())
                .status(updated.getStatus())
                .ownerName(updated.getOwner().getName())
                .categoryName(updated.getCategory().getName())
                .serviceAreaName(updated.getServiceArea().getName())
                .build();
    }
}
