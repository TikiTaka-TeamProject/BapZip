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
     * 주소 기반 호출
     */
    public KakaoLocalResponseDto getResponse(String query){
        if (query == null || query.trim().isEmpty()){
            throw new NullPointerException("query 값이 비어있습니다.");
        }

        JsonObject document = kakaoLocalCallable.getDocuments(query);
        if (document == null) {
            throw new KakaoLocalResponseNotFoundException(ErrorCode.KAKAO_MAP_DOCUMENTS_NOT_FOUND);
        }

        JsonObject address = null;
        if (document.has("address") && !document.get("address").isJsonNull()) {
            address = document.getAsJsonObject("address");
        } else if (document.has("road_address") && !document.get("road_address").isJsonNull()) {
            address = document.getAsJsonObject("road_address");
        }

        if (address == null) {
            throw new KakaoLocalResponseNotFoundException(ErrorCode.KAKAO_MAP_ADDRESS_NOT_FOUND);
        }

        return KakaoLocalResponseDto.from(document, address);
    }

    /**
     * 좌표 기반 호출
     */
    public KakaoLocalResponseDto getAddressFromCoordinates(Double longitude, Double latitude) {
        if (longitude == null || latitude == null) {
            throw new NullPointerException("좌표 값이 비어있습니다.");
        }

        JsonObject document = kakaoLocalCallable.getDocumentsByCoordinates(longitude, latitude);
        if (document == null) {
            throw new KakaoLocalResponseNotFoundException(ErrorCode.KAKAO_MAP_DOCUMENTS_NOT_FOUND);
        }

        try {
            return KakaoLocalResponseDto.fromCoordinates(document);
        } catch (IllegalArgumentException e) {
            throw new KakaoLocalResponseNotFoundException(ErrorCode.KAKAO_MAP_ADDRESS_NOT_FOUND);
        }
    }
}
