package com.sparta.bapzip.shop.domain.entity;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.UUID;


@Entity
@Table(name = "p_shops")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShopEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopStatus status = ShopStatus.PENDING;

//    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private UserEntity owner;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @JoinColumn(name = "service_area_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceAreaEntity serviceArea;

    public ShopEntity(String name, String address, Point location,
                      UserEntity owner, CategoryEntity category, ServiceAreaEntity serviceArea) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.owner = owner;
        this.category = category;
        this.serviceArea = serviceArea;
    }


}
