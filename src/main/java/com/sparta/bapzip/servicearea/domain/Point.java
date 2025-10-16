package com.sparta.bapzip.servicearea.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "위도,경도를 갖는 포인트 클래스")
public class Point {

    @Schema(description = "위도", example = "37.583597")
    private Double x;

    @Schema(description = "경도", example = "126.969919")
    private Double y;
}

