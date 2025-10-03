package com.sparta.bapzip.shop.presentation.dto.response;

import com.sparta.bapzip.shop.domain.entity.ShopStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CreateShopResponse {
    private UUID shopId;            // 샵 고유 ID
    private String categoryName;    // 샵 카테고리
    private String name;            // 샵 이름
    private String serviceAreaName; // 서비스 지역 이름
    private String address;         // 샵 주소
    private ShopStatusEnum status;      // 현재 상태 (PENDING 등)
}