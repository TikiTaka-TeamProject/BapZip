package com.sparta.bapzip.shop.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ResponseDto {
    private UUID id;
    private String name;
    private String address;
    private String status;
    private Double longitude;
    private Double latitude;
}
