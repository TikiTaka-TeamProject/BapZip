package com.sparta.bapzip.servicearea.application.dto;

import com.sparta.bapzip.servicearea.domain.Point;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AreaReturnDto {
    private final UUID id;
    private final String name;
    private final List<Point> area;
    private final Boolean isService;

    public static AreaReturnDto from(ServiceAreaEntity areaEntity){
        return AreaReturnDto.builder()
                .id(areaEntity.getId())
                .name(areaEntity.getName())
                .area(polygonList(areaEntity.getArea()))
                .isService(areaEntity.isService())
                .build();
    }

    private static List<Point> polygonList(Polygon polygon) {
        List<Point> points = new ArrayList<>();
        Coordinate[] coordinates = polygon.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            points.add(new Point(coordinate.getX(), coordinate.getY()));
        }
        return points;
    }
}
