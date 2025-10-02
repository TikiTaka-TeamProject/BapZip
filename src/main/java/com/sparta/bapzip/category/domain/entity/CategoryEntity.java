package com.sparta.bapzip.category.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_categories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<ShopEntity> shopEntityList = new ArrayList<>();


}
