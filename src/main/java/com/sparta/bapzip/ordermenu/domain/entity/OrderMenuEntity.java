package com.sparta.bapzip.ordermenu.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.application.dto.request.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_orders_menus")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderMenuEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int subtotal;

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

    @JoinColumn(name = "menu_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuEntity menu;

    // 비즈니스 로직

    /**
     * 주문 메뉴 생성
     */
    public static OrderMenuEntity create(
            OrderEntity order,
            MenuEntity menu,
            CreateOrderRequest.MenuInfo menuInfo
    ) {
        int subtotal = menu.getPrice() * menuInfo.getQuantity();

        return OrderMenuEntity.builder()
                .order(order)
                .menu(menu)
                .quantity(menuInfo.getQuantity())
                .name(menu.getName())
                .price(menu.getPrice())
                .subtotal(subtotal)
                .build();
    }

    // 연관관계 설정 로직

    /**
     * 부모 엔티티인 Order 설정
     */
    public void addOrder(OrderEntity order) {
        this.order = order;
    }
}