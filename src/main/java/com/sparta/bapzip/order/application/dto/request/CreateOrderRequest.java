package com.sparta.bapzip.order.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateOrderRequest {

    @NotNull
    private UUID shopId;
    @NotBlank
    private String deliveryAddress;
    @NotBlank
    private String detailAddress;
    @NotBlank
    private String customerPhone;
    @NotNull
    private List<MenuInfo> menuInfoList;
    @NotBlank
    private String paymentType;
    private String specialRequest;

    @Getter
    public static class MenuInfo {
        @NotBlank
        private UUID menuId;
        @NotBlank
        private int quantity;
    }
}