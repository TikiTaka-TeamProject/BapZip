package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopDetailForUserResponse {
    private UUID shopId;
    private String name;
    private String address;
    private String ownerName;
    private String categoryName;
    private double avgScore;

    public static ShopDetailForUserResponse from(ShopEntity shop) {
        return ShopDetailForUserResponse.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .ownerName(shop.getOwner().getName())
                .categoryName(shop.getCategory().getName())
                .avgScore(0.0)
                .build();
    }
}
