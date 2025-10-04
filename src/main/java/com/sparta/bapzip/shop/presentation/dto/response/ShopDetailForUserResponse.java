package com.sparta.bapzip.shop.presentation.dto.response;

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
    private String serviceAreaName;
}
