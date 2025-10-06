package com.sparta.bapzip.menu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_menus")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MenuStatus status = MenuStatus.AVAILABLE;

    @JoinColumn(name = "shop_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity shop;

    @JsonIgnore
    @OneToMany(mappedBy = "menu")
    @Builder.Default
    private List<OrderMenuEntity> orderMenuList = new ArrayList<>();


}
