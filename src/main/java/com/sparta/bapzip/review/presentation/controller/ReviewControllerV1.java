package com.sparta.bapzip.review.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.review.application.ReviewServiceV1;
import com.sparta.bapzip.review.application.dto.request.CreateReviewRequest;
import com.sparta.bapzip.review.presentation.dto.response.ReviewCreateResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Review 관련 API를 처리하는 컨트롤러 (v1)
 * <p>
 * 리뷰 작성, 조회 등 리뷰 관련 요청을 처리합니다.
 * 현재는 리뷰 생성 API만 제공됩니다.
 * </p>
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reviews")
public class ReviewControllerV1 {

    private final ReviewServiceV1 reviewServiceV1;

    /**
     * 새로운 리뷰를 작성합니다.
     * <p>
     * 사용자의 인증 정보와 요청 DTO를 받아 리뷰를 생성하고,
     * 생성된 리뷰 정보를 포함한 {@link ReviewCreateResponse}를 반환합니다.
     * </p>
     *
     * @param createReviewRequest 리뷰 작성에 필요한 정보(점수, 내용, 주문/가게 ID 등)를 담은 DTO
     * @param userDetails 인증된 사용자 정보({@link UserDetailsImpl})
     * @return 생성된 리뷰 정보를 포함한 {@link ApiResponse} 객체, HTTP 상태 코드 201(CREATED)
     * @throws IllegalStateException 이미 해당 주문에 대한 리뷰가 존재하는 경우
     * @throws IllegalArgumentException 존재하지 않는 주문 또는 가게 ID가 전달된 경우
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewCreateResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest createReviewRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewCreateResponse response = reviewServiceV1.createReview(userDetails.getUser(), createReviewRequest);
        return ApiResponse.created(response);
    }
}
