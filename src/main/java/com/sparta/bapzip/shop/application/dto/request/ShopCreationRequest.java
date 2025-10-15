package com.sparta.bapzip.shop.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ShopCreationRequest {
    @NotNull(message = "가게 이름은 필수입니다.")
    private String name;

    @NotNull(message = "가게 주소는 필수입니다.")
    private String address;

    @NotNull(message = "오너 ID는 필수입니다.")
    private Long ownerId;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    private UUID categoryId;
}

