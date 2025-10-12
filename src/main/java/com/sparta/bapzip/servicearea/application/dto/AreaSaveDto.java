package com.sparta.bapzip.servicearea.application.dto;

import com.sparta.bapzip.servicearea.application.dto.request.AreaSaveRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import com.sparta.bapzip.servicearea.domain.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

@Getter
@Setter
@Builder
public class AreaSaveDto {
    private final String name;
    private final Polygon area;

    public static AreaSaveDto from(AreaSaveRequest request){
        return AreaSaveDto.builder()
                .name(request.getName())
                .area(listPolygon(request.getArea()))
                .build();
    }

    private static Polygon listPolygon(List<Point> area) {
        GeometryFactory geometryFactory = new GeometryFactory();
        int lenOfPolygon = area.size();
        Coordinate[] coordinates = new Coordinate[lenOfPolygon + 1];
        for (int i = 0; i < lenOfPolygon; i++) {
            Point point = area.get(i);
            coordinates[i] = new Coordinate(point.getX(), point.getY());
        }
        coordinates[lenOfPolygon] = coordinates[0];
        return geometryFactory.createPolygon(coordinates);
    }
}