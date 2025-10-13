package com.sparta.bapzip.servicearea.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.sparta.bapzip.servicearea.domain.Point;

import java.util.List;

@Getter
@AllArgsConstructor
public class AreaSaveRequest {
    private String name;
    private List<Point> area;
}
