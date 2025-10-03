package com.sparta.bapzip.shop.domain.entity;

public enum ShopStatus {
    PENDING,    // 등록 후 승인 대기
    APPROVED,   // 매니저 승인 완료
    REJECTED    // 매니저 승인 거부
}
