package com.sparta.bapzip.menu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.menu.domain.enums.MenuStatus;
import com.sparta.bapzip.menu.application.dto.request.MenuCreateRequest;
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
public class MenuEntity extends BaseEntity {

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

    // 비즈니스 로직

    public static MenuEntity createMenu(MenuCreateRequest request, ShopEntity shop) {
        return MenuEntity.builder()
                .name(request.name())
                .content(request.content())
                .price(request.price())
                .shop(shop)
                .status(MenuStatus.AVAILABLE)
                .build();
    }

    /**
     * 메뉴가 품절인지 확인
     */
    public boolean isSoldOut() {
        return this.status == MenuStatus.SOLD_OUT;
    }
  
    /**
     * update Filed
     */
    public void updateName(String name) {
        this.name = name;
    }
    public void updateContent(String content) {
        this.content = content;
    }
    public void updatePrice(int price) {
        this.price = price;
    }
    public void updateStatus(MenuStatus status) {
        this.status = status;
    }

    /**
     * menu soft delete
     */
    public void deleteMenu(Long userId){
        markDeleted(userId);
    }

}
