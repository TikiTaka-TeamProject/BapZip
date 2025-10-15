package com.sparta.bapzip.shop.application.dto.request;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor(access  = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopUpdateRequest {
    private String name;
    private String address;
    private UUID categoryId;
}