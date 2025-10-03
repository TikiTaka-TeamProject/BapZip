package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;
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
}
