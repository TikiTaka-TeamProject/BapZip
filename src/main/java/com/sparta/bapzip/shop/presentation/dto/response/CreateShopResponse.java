package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateShopResponse {
    private UUID shopId;            // 샵 고유 ID
    private String categoryName;    // 샵 카테고리
    private String name;            // 샵 이름
    private String address;         // 샵 주소
    private ShopStatusEnum status;      // 현재 상태 (PENDING 등)

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