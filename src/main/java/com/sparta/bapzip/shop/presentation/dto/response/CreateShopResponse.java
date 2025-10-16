package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.*;

import java.util.UUID;

/**
 * Shop 생성 API 응답 DTO
 * <p>
 * 새로운 Shop 생성 시 반환되는 정보
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateShopResponse {

    /** Shop 고유 ID */
    private UUID shopId;

    /** Shop 카테고리 이름 */
    private String categoryName;

    /** Shop 이름 */
    private String name;

    /** Shop 주소 */
    private String address;

    /** Shop 상태 (PENDING, APPROVED 등) */
    private ShopStatusEnum status;

    /**
     * ShopEntity로부터 CreateShopResponse DTO 생성
     *
     * @param shop ShopEntity
     * @return CreateShopResponse DTO
     */
    public static CreateShopResponse from(ShopEntity shop) {
        return CreateShopResponse.builder()
                .shopId(shop.getId())
                .categoryName(shop.getCategory().getName())
                .name(shop.getName())
                .address(shop.getAddress())
                .status(shop.getStatus())
                .build();
    }
}
