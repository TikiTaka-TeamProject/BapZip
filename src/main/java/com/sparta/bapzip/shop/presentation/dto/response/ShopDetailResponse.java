package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.*;

import java.util.UUID;

/**
 * 관리자용 Shop 상세 정보 DTO
 * <p>
 * 가게 상세 조회 시 반환되는 DTO로, 관리자 또는 소유자가 확인할 수 있는 정보를 포함
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopDetailResponse {

    /** Shop 고유 ID */
    private UUID shopId;

    /** Shop 이름 */
    private String name;

    /** Shop 주소 */
    private String address;

    /** Shop 상태 (PENDING, APPROVED 등) */
    private ShopStatusEnum status;

    /** Shop 소유자 이름 */
    private String ownerName;

    /** Shop 카테고리 이름 */
    private String categoryName;

    /** Shop 평균 평점 */
    private double avgScore;

    /**
     * ShopEntity로부터 ShopDetailResponse DTO 생성
     *
     * @param shop ShopEntity
     * @return ShopDetailResponse DTO
     */
    public static ShopDetailResponse from(ShopEntity shop) {
        return ShopDetailResponse.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .status(shop.getStatus())
                .ownerName(shop.getOwner().getName())
                .categoryName(shop.getCategory().getName())
                .avgScore(shop.getAvgScore())
                .build();
    }
}
