package com.sparta.bapzip.shop.presentation.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access  = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopUpdateRequest {
    private String name;
    private String address;
    private String categoryId;
    private String serviceAreaId;
    private Double longitude;
    private Double latitude;
}