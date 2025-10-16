package com.sparta.bapzip.servicearea.application.dto.request;

import com.sparta.bapzip.servicearea.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "서비스지역 등록 요청형식")
public class AreaSaveRequest {

    @Schema(description = "이름", example = "광화문 지역")
    private String name;

    @Schema(description = "POLYGON geometry에 들어갈 Point 좌표 리스트", example =
            "[{\n" +
            "                    \"x\": 37.583597,\n" +
            "                    \"y\": 126.969919\n" +
            "                },\n" +
            "                {\n" +
            "                    \"x\": 37.583469,\n" +
            "                    \"y\": 126.981209\n" +
            "                },\n" +
            "                {\n" +
            "                    \"x\": 37.574448,\n" +
            "                    \"y\": 126.981070\n" +
            "                },\n" +
            "                {\n" +
            "                    \"x\": 37.573268,\n" +
            "                    \"y\": 126.969774\n" +
            "                }]")
    private List<Point> area;
}
