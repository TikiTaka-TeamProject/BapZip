package com.sparta.bapzip.servicearea.presentation.dto.response;

import com.sparta.bapzip.servicearea.application.dto.AreaReturnDto;
import com.sparta.bapzip.servicearea.domain.Point;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class AreaSaveResponse {

    private final UUID serviceAreaId;
    private final String areaName;
    private final List<Point> pointList;
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
