package com.sparta.bapzip.order.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_orders")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String paymentType;

    @Column(nullable = false)
    private int menuTotalPrice;

    @Column(nullable = false)
    private int totalAmount;

    private String specialRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    @Builder. Default
    private List<OrderMenuEntity> orderMenuList = new ArrayList<>();

}
