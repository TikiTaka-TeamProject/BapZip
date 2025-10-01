package com.sparta.bapzip.payment.domain.entity;

import jakarta.persistence.*;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
