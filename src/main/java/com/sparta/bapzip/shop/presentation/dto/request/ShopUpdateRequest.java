package com.sparta.bapzip.shop.presentation.dto.request;

import lombok.Getter;

@Getter
public class ShopUpdateRequest {
    private String name;
    private String address;
    private String categoryId;
    private String serviceAreaId;
    private Double longitude;
    private Double latitude;
}