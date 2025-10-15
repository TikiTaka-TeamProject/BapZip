package com.sparta.bapzip.review.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateReviewRequest {

    @NotNull
    private Integer score; // 새로운 평점

    @NotBlank
    private String content; // 새로운 리뷰 내용

}
