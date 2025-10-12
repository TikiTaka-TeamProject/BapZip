package com.sparta.bapzip.order.application.dto;

import com.sparta.bapzip.ordermenu.domain.entity.OrderMenuEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderMenuInfo {

    private final UUID menuId;
    private final String menuName;
    private final int price;
    private final int quantity;
    private final int subtotal;

    public static OrderMenuInfo from(OrderMenuEntity entity) {
        return OrderMenuInfo.builder()
                .menuId(entity.getMenu().getId())
                .menuName(entity.getName())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .subtotal(entity.getSubtotal())
                .build();
    }
}
