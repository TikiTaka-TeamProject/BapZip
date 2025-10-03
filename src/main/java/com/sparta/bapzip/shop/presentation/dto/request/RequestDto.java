package com.sparta.bapzip.shop.presentation.dto.request;


import com.sparta.bapzip.shop.domain.entity.ShopStatus;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@Getter
public class RequestDto {
    private String name;             // 가게 이름
    private String address;          // 가게 주소
    private Double longitude;        // x
    private Double latitude;         // y
    private ShopStatus status;
    private Long ownerId;            // 오너 유저 ID
    private UUID categoryId;         // 카테고리 ID
    private UUID serviceAreaId;      // 서비스 지역 ID
}
