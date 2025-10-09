package com.sparta.bapzip.kakaomap.application;

import com.google.gson.JsonObject;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.kakaomap.application.dto.KaKaoMapResponseDto;
import com.sparta.bapzip.kakaomap.domain.exception.KakaoMapResponseNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoMapServiceV1 {

    private final KakaoMapCallable kakaoMapCallable;

    /**
     * <p> 카카오맵 주소입력시 반환 data </p>
     * @addressName : 전체 지번 주소 또는 전체 도로명 주소, 입력에 따라 결정됨
     * @addressType : address_name의 값의 타입(Type) REGION(지명), ROAD(도로명), REGION_ADDR(지번 주소), ROAD_ADDR(도로명 주소)
     * @longitude : X 좌표값, 경위도인 경우 경도
     * @latitude : Y 좌표값, 경위도인 경우 위도
     * @hCode : 행정 코드
     * @bCode : 법정 코드
     * @return KaKaoMapResponseDto
     */
    public KaKaoMapResponseDto getResponse(String query){
        // query 가 비어있는경우
        if (query == null || query.trim().isEmpty()){
            throw new NullPointerException("query 값이 비어있습니다.");
        }
        JsonObject documents = kakaoMapCallable.getDocuments(query);
        //응답 documents가 없을경우
        if (documents == null){
            throw new KakaoMapResponseNotFound(ErrorCode.KAKAO_MAP_DOCUMENTS_NOT_FOUND);
        }
        //응답 address가 없을경우
        if (documents.get("address").isJsonNull()){
            throw new KakaoMapResponseNotFound(ErrorCode.KAKAO_MAP_ADDRESS_NOT_FOUND);
        }
        JsonObject address = documents.get("address").getAsJsonObject();
        return KaKaoMapResponseDto.from(documents, address);
    }
}
