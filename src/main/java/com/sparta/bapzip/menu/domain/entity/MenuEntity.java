package com.sparta.bapzip.menu.domain.entity;

import jakarta.persistence.*;

@Entity
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
