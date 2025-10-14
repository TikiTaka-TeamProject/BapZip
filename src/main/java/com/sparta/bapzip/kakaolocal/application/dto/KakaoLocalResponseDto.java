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

//    public static KakaoLocalResponseDto from(JsonObject document, JsonObject address){
//        return new KakaoLocalResponseDto(
//                document.get("address_name").getAsString(),
//                document.get("address_type").getAsString(),
//                document.get("x").getAsString(),
//                document.get("y").getAsString(),
//                address.get("h_code").getAsString(),
//                address.get("b_code").getAsString()
//        );
//    }

    public static KakaoLocalResponseDto from(JsonObject document, JsonObject address) {
        String addressName;
        String addressType;

        if (!document.get("address_name").isJsonNull() && document.has("address_name")) {
            addressName = document.get("address_name").getAsString();
        } else if (!document.get("road_address_name").isJsonNull() && document.has("road_address_name")) {
            addressName = document.get("road_address_name").getAsString();
        } else {
            addressName = ""; // 주소 정보가 없으면 빈 문자열 처리
        }

        if (!document.get("address_type").isJsonNull() && document.has("address_type")) {
            addressType = document.get("address_type").getAsString();
        } else if (!document.get("road_address_type").isJsonNull() && document.has("road_address_type")) {
            addressType = document.get("road_address_type").getAsString();
        } else {
            addressType = ""; // 타입 정보가 없으면 빈 문자열 처리
        }

        return new KakaoLocalResponseDto(
                addressName,
                addressType,
                document.get("x").getAsString(),
                document.get("y").getAsString(),
                address.has("h_code") ? address.get("h_code").getAsString() : "",
                address.has("b_code") ? address.get("b_code").getAsString() : ""
        );
    }
}
