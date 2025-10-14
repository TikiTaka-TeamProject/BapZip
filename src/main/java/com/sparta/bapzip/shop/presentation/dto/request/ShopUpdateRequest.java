package com.sparta.bapzip.shop.presentation.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access  = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopUpdateRequest {
    private String name;
    private String address;
    private UUID categoryId;
    private Double longitude;
    private Double latitude;
}