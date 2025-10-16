package com.sparta.bapzip.servicearea.presentation.dto.response;

import com.sparta.bapzip.servicearea.application.dto.AreaReturnDto;
import com.sparta.bapzip.servicearea.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "서비스지역 등록 응답형식")
public class AreaSaveResponse {

    @Schema(description = "서비스 지역 식별자", example = "7294b430-57f8-467b-b6c1-09a3ceae3186")
    private final UUID serviceAreaId;

    @Schema(description = "서비스 지역이름", example = "광화문 지역")
    private final String areaName;

    @Schema(description = "서비스 지역 POLYGON data")
    private final List<Point> pointList;

    @Schema(description = "서비스 지역 활성화 상태", example = "true")
    private final Boolean isService;

    public static AreaSaveResponse from(AreaReturnDto dto){
        return AreaSaveResponse.builder()
                .serviceAreaId(dto.getId())
                .areaName(dto.getName())
                .pointList(dto.getArea())
                .isService(dto.getIsService())
                .build();
    }

}
