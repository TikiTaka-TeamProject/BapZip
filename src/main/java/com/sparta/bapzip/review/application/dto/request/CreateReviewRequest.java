package com.sparta.bapzip.review.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateReviewRequest {

    @NotNull
    private Integer score;

    @NotBlank
    private String content;

    @NotNull
    private UUID shopId;

    @NotNull
    private UUID orderId;
}
