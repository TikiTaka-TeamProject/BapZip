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

    public static KakaoLocalResponseDto from(JsonObject document, JsonObject address){
        return new KakaoLocalResponseDto(
                document.get("address_name").getAsString(),
                document.get("address_type").getAsString(),
                document.get("x").getAsString(),
                document.get("y").getAsString(),
                address.get("h_code").getAsString(),
                address.get("b_code").getAsString()
        );
    }
}
