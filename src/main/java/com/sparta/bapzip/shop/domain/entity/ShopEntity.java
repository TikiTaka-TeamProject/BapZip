package com.sparta.bapzip.shop.domain.entity;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.UUID;


@Entity
@Table(name = "p_shops")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private ShopStatusEnum status = ShopStatusEnum.PENDING;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
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

    public void updateName(String name) { this.name = name; }
    public void updateAddress(String address) { this.address = address; }
    public void updateLocation(Point location) { this.location = location; }
    public void updateCategory(CategoryEntity category) { this.category = category; }
    public void updateServiceArea(ServiceAreaEntity serviceArea) { this.serviceArea = serviceArea; }



}
