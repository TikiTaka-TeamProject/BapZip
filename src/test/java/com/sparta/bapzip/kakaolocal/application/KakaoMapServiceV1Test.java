package com.sparta.bapzip.kakaolocal.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.kakaolocal.application.dto.KakaoLocalResponseDto;
import com.sparta.bapzip.kakaolocal.domain.exception.KakaoLocalResponseNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class KakaoMapServiceV1Test {

    @Autowired
    KakaoLocalServiceV1 serviceV1;

    @Test
    void getResponse() {

        String query = "돌곶이로 41길 24";
        KakaoLocalResponseDto responseDto = serviceV1.getResponse(query);
        System.out.println("addressName = "+ responseDto.getAddressName());
        System.out.println("addressType = "+ responseDto.getAddressType());
        System.out.println("longitude = "+ responseDto.getLongitude());
        System.out.println("latitude = "+ responseDto.getLatitude());
        System.out.println("hCode = "+ responseDto.getHCode());
        System.out.println("bCode = "+ responseDto.getBCode());
    }

    @DisplayName("query값이 비어있을경우 예외처리")
    @Test
    void test1(){
        String query = "";
        NullPointerException exception = assertThrows(NullPointerException.class, () -> serviceV1.getResponse(query));
        assertEquals("query 값이 비어있습니다.", exception.getMessage());
    }

    @DisplayName("응답 documents가 없을경우 예외처리")
    @Test
    void test2(){
        String query = "123";
        KakaoLocalResponseNotFound exception = assertThrows(KakaoLocalResponseNotFound.class,()->serviceV1.getResponse(query));
        assertEquals(ErrorCode.KAKAO_MAP_DOCUMENTS_NOT_FOUND,exception.getErrorCode());
    }

    @DisplayName("응답 address가 없을경우 예외처리")
    @Test
    void test3(){
        String query = "돌곶이로";
        KakaoLocalResponseNotFound exception = assertThrows(KakaoLocalResponseNotFound.class,()->serviceV1.getResponse(query));
        assertEquals(ErrorCode.KAKAO_MAP_ADDRESS_NOT_FOUND,exception.getErrorCode());
    }


}