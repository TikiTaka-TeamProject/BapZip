package com.sparta.bapzip.review.domain.entity;

import jakarta.persistence.*;

@Entity
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
