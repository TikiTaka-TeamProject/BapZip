package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import lombok.*;

import java.util.UUID;

/**
 * 사용자용 Shop 상세 정보 DTO
 * <p>
 * 가게 목록 조회나 검색 시 사용자에게 반환되는 가게 정보
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopDetailForUserResponse {

    /** Shop 고유 ID */
    private UUID shopId;

    /** Shop 이름 */
    private String name;

    /** Shop 주소 */
    private String address;

    /** Shop 소유자 이름 */
    private String ownerName;

    /** Shop 카테고리 이름 */
    private String categoryName;

    /** Shop 평균 평점 */
    private double avgScore;

    /**
     * ShopEntity로부터 ShopDetailForUserResponse DTO 생성
     *
     * @param shop ShopEntity
     * @return ShopDetailForUserResponse DTO
     */
    public static ShopDetailForUserResponse from(ShopEntity shop) {
        return ShopDetailForUserResponse.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .ownerName(shop.getOwner().getName())
                .categoryName(shop.getCategory().getName())
                .avgScore(shop.getAvgScore())
                .build();
    }
}
