package com.sparta.bapzip.kakaolocal.application;

import com.google.gson.JsonObject;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.kakaolocal.application.dto.KakaoLocalResponseDto;
import com.sparta.bapzip.kakaolocal.application.exception.KakaoLocalResponseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLocalServiceV1 {

    private final KakaoLocalCallable kakaoLocalCallable;

    /**
     * <p> 카카오맵 주소입력시 반환 data </p>
     * @param query 검색할 주소
     * @addressName : 전체 지번 주소 또는 전체 도로명 주소, 입력에 따라 결정됨
     * @addressType : address_name의 값의 타입(Type) REGION(지명), ROAD(도로명), REGION_ADDR(지번 주소), ROAD_ADDR(도로명 주소)
     * @longitude : X 좌표값, 경위도인 경우 경도
     * @latitude : Y 좌표값, 경위도인 경우 위도
     * @hCode : 행정 코드
     * @bCode : 법정 코드
     * @return KaKaoMapResponseDto
     */
    public KakaoLocalResponseDto getResponse(String query){
        // query 가 비어있는경우
        if (query == null || query.trim().isEmpty()){
            throw new NullPointerException("query 값이 비어있습니다.");
        }
        JsonObject documents = kakaoLocalCallable.getDocuments(query);
        //응답 documents가 없을경우
        if (documents == null){
            throw new KakaoLocalResponseNotFoundException(ErrorCode.KAKAO_MAP_DOCUMENTS_NOT_FOUND);
        }
        // 지번/도로명 주소 fallback 처리
        JsonObject address = null;
        if (documents.has("address") && !documents.get("address").isJsonNull()) {
            address = documents.get("address").getAsJsonObject();
        } else if (documents.has("road_address") && !documents.get("road_address").isJsonNull()) {
            address = documents.get("road_address").getAsJsonObject();
        }

        if (address == null) {
            throw new KakaoLocalResponseNotFoundException(ErrorCode.KAKAO_MAP_ADDRESS_NOT_FOUND);
        }
        return KakaoLocalResponseDto.from(documents, address);
    }
}
