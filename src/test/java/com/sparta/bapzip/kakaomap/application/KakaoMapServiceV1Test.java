package com.sparta.bapzip.kakaomap.application;

import com.sparta.bapzip.kakaomap.application.dto.KaKaoMapResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoMapServiceV1Test {

    @Autowired
    KakaoMapServiceV1 serviceV1;

    @Test
    void getResponse() {

        String query = "돌곶이로 41길 24";
        KaKaoMapResponseDto responseDto = serviceV1.getResponse(query);
        System.out.println("addressName = "+ responseDto.getAddressName());
        System.out.println("addressType = "+ responseDto.getAddressType());
        System.out.println("longitude = "+ responseDto.getLongitude());
        System.out.println("latitude = "+ responseDto.getLatitude());
        System.out.println("hCode = "+ responseDto.getHCode());
        System.out.println("bCode = "+ responseDto.getBCode());
    }
}