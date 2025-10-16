package com.sparta.bapzip.shop.application.dto;

import java.util.UUID;

public interface ShopWithAvgScoreDto {
    UUID getShopId();
    double getAvgScore();
}