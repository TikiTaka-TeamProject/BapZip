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
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "order_id")
    @OneToOne
    private OrderEntity order;

    @Column(name = "payment_key", length = 255)
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

    @Column(name = "cancel_reason")
    private String cancelReason;

    public void updatePaymentConfirmResult(String paymentKey, PaymentStatusEnum status, LocalDateTime approvedAt) {
        this.paymentKey = paymentKey;
        this.status = status;
        this.approvedAt = approvedAt;
    }
    public void updatePaymentCancelResult(PaymentStatusEnum status, String cancelReason, LocalDateTime canceledAt) {
        this.status = PaymentStatusEnum.CANCELED;
        this.cancelReason = cancelReason;
        this.canceledAt = canceledAt;
    }

    @Override
    public String toString() {
        return "PaymentEntity{" +
                "id=" + id +
                ", paymentKey='" + paymentKey + '\'' +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", approvedAt=" + approvedAt +
                ", canceledAt=" + canceledAt +
                ", cancelReason='" + cancelReason + '\'' +
                '}';
    }
}
