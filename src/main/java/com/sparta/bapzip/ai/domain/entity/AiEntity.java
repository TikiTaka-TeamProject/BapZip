package com.sparta.bapzip.ai.domain.entity;

import jakarta.persistence.*;

@Entity
public class AiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
