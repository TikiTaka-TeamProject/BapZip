package com.sparta.bapzip.shop.presentation.dto.request;

import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatShopRequest {
    @NotNull(message = "가게 이름은 필수입니다.")
    private String name;

    @NotNull(message = "가게 주소는 필수입니다.")
    private String address;

    @NotNull(message = "경도(x)는 필수입니다.")
    private Double longitude;

    @NotNull(message = "위도(y)는 필수입니다.")
    private Double latitude;

    private ShopStatusEnum status;

    @NotNull(message = "오너 ID는 필수입니다.")
    private Long ownerId;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    private UUID categoryId;

    @NotNull(message = "서비스 지역 ID는 필수입니다.")
    private UUID serviceAreaId;
}

