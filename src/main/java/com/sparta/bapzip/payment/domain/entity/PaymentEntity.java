package com.sparta.bapzip.payment.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_payments")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class PaymentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "order_id", nullable = false)
    @OneToOne
    private OrderEntity order;

    @Column(name = "payment_key", length = 255, updatable = false)
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatusEnum status;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "cancel_reason", length = 250)
    private String cancelReason;

    public PaymentEntity(
                        OrderEntity order,
                         String paymentKey,
                         PaymentStatusEnum status,
                         int totalAmount,
                         LocalDateTime approvedAt, LocalDateTime canceledAt, String cancelReason) {

        this.order = order;
        this.paymentKey = paymentKey;
        this.status = status;
        this.totalAmount = totalAmount;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.cancelReason = cancelReason;
    }

    public PaymentEntity(UUID orderId, String paymentKey, PaymentStatusEnum status, int amount, LocalDateTime now,  LocalDateTime canceledAt, String cancelReason) {

        this.paymentKey = paymentKey;
        this.status = status;
        this.totalAmount = totalAmount;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.cancelReason = cancelReason;
    }
}
