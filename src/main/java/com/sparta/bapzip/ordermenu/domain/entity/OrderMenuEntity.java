package com.sparta.bapzip.ordermenu.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_orders_menus")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

    @JoinColumn(name = "menu_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuEntity menu;




}
