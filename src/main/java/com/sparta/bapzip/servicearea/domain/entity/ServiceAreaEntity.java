package com.sparta.bapzip.servicearea.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

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

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String code;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geom;

    @Column(columnDefinition = "boolean default true")
    private boolean isService;

    @OneToMany(mappedBy = "serviceArea")
    @JsonIgnore
    private List<ShopEntity> shopList = new ArrayList<>();

}
