package com.sparta.bapzip.servicearea.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.servicearea.application.dto.AreaSaveDto;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_service_areas")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAreaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition="geometry(Polygon,4326)")
    protected Polygon area;

    @Column(columnDefinition = "boolean default true")
    private boolean isService = true;

    public static ServiceAreaEntity create(AreaSaveDto request){
        return ServiceAreaEntity.builder()
                .name(request.getName())
                .area(request.getArea())
                .isService(true)
                .build();
    }

}
