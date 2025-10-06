package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ShopDetailResponse {
    private UUID shopId;
    private String name;
    private String address;
    private ShopStatusEnum status;
    private String ownerName;
    private String categoryName;
    private String serviceAreaName;

    public static ShopDetailResponse from(ShopEntity shop) {
        return ShopDetailResponse.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .status(shop.getStatus())
                .ownerName(shop.getOwner().getName())
                .categoryName(shop.getCategory().getName())
                .serviceAreaName(shop.getServiceArea().getName())
                .build();
    }
}
