package com.sparta.bapzip.kakaolocal.application.dto;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class KakaoLocalResponseDto {

    private final String addressName;
    private final String addressType;
    private final String longitude;
    private final String latitude;
    private final String hCode;
    private final String bCode;

    /**
     * 주소 기반 호출용 안전 생성 메서드
     */
    public static KakaoLocalResponseDto from(JsonObject document, JsonObject address) {
        if (document == null || address == null) {
            throw new IllegalArgumentException("Kakao API response is invalid");
        }

        String addressName = "";
        String addressType = "";

        // 지번/도로명 fallback 처리
        if (address.has("address_name") && !address.get("address_name").isJsonNull()) {
            addressName = address.get("address_name").getAsString();
        } else if (address.has("road_address_name") && !address.get("road_address_name").isJsonNull()) {
            addressName = address.get("road_address_name").getAsString();
        }

        if (address.has("address_type") && !address.get("address_type").isJsonNull()) {
            addressType = address.get("address_type").getAsString();
        } else if (address.has("road_address_type") && !address.get("road_address_type").isJsonNull()) {
            addressType = address.get("road_address_type").getAsString();
        }

        String longitude = document.has("x") && !document.get("x").isJsonNull() ? document.get("x").getAsString() : "";
        String latitude = document.has("y") && !document.get("y").isJsonNull() ? document.get("y").getAsString() : "";
        String hCode = address.has("h_code") && !address.get("h_code").isJsonNull() ? address.get("h_code").getAsString() : "";
        String bCode = address.has("b_code") && !address.get("b_code").isJsonNull() ? address.get("b_code").getAsString() : "";

        return new KakaoLocalResponseDto(addressName, addressType, longitude, latitude, hCode, bCode);
    }

    /**
     * 좌표 기반 호출용 안전 생성 메서드
     */
    public static KakaoLocalResponseDto fromCoordinates(JsonObject document) {
        if (document == null || !document.has("address") || document.get("address").isJsonNull()) {
            throw new IllegalArgumentException("Kakao API response is invalid");
        }

        JsonObject address = document.getAsJsonObject("address");

        String addressName = address.has("address_name") && !address.get("address_name").isJsonNull()
                ? address.get("address_name").getAsString() : "";
        String addressType = address.has("address_type") && !address.get("address_type").isJsonNull()
                ? address.get("address_type").getAsString() : "";

        String longitude = document.has("x") && !document.get("x").isJsonNull() ? document.get("x").getAsString() : "";
        String latitude = document.has("y") && !document.get("y").isJsonNull() ? document.get("y").getAsString() : "";
        String hCode = address.has("h_code") && !address.get("h_code").isJsonNull() ? address.get("h_code").getAsString() : "";
        String bCode = address.has("b_code") && !address.get("b_code").isJsonNull() ? address.get("b_code").getAsString() : "";

        return new KakaoLocalResponseDto(addressName, addressType, longitude, latitude, hCode, bCode);
    }
}
