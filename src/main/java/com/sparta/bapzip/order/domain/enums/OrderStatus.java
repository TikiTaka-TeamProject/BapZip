package com.sparta.bapzip.order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("주문요청"),
    ACCEPTED("주문수락"),
    REJECTED("주문거절"),
    COOKING("조리중"),
    COOK_COMPLETED("조리완료"),
    DELIVERING("배달중"),
    DELIVERED("배달완료"),
    COMPLETED("주문완료");

    private final String description;
}