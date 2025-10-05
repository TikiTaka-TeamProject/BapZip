package com.sparta.bapzip.review.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_reviews")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "user_id", nullable = false )
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "shop_id", nullable = false )
    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity shop;

    @JoinColumn(name = "order_id", nullable = false )
    @OneToOne
    private OrderEntity order;


}
