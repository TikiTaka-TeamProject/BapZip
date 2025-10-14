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

/**
 * 리뷰(Review) 엔티티를 나타내는 클래스입니다.
 *
 * <p>리뷰는 특정 주문(Order), 사용자(User), 그리고 가게(Shop)에 연결되어 있으며,
 * 점수(score)와 내용(content)을 포함합니다. {@link BaseEntity}를 상속받아
 * 생성일, 수정일 등의 공통 필드를 포함합니다.</p>
 */
@Entity
@Table(name = "p_reviews")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends BaseEntity {

    /**
     * 리뷰의 고유 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * 리뷰 점수
     */
    @Column(nullable = false)
    private int score;

    /**
     * 리뷰 내용
     */
    @Column(nullable = false)
    private String content;

    /**
     * 리뷰를 작성한 사용자
     */
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    /**
     * 리뷰 대상 가게
     */
    @JoinColumn(name = "shop_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ShopEntity shop;

    /**
     * 리뷰가 연결된 주문
     */
    @JoinColumn(name = "order_id", nullable = false)
    @OneToOne
    private OrderEntity order;
}