package com.sparta.bapzip.payment.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_payments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "order_id", nullable = false)
    @OneToOne
    private OrderEntity order;



}
