package com.sparta.bapzip.review.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.review.application.ReviewServiceV1;
import com.sparta.bapzip.review.application.dto.ReviewDto;
import com.sparta.bapzip.review.application.dto.request.CreateReviewRequest;
import com.sparta.bapzip.review.application.dto.request.UpdateReviewRequest;
import com.sparta.bapzip.review.presentation.dto.response.ReviewCreateResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    /**
     * 특정 가게에 작성된 모든 리뷰를 조회합니다.
     *
     * @param shopId 조회할 가게의 ID
     * @return 해당 가게의 리뷰 리스트
     */
    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByShop(@PathVariable String shopId) {
        List<ReviewDto> reviews = reviewServiceV1.getReviewsByShop(shopId);
        return ApiResponse.ok(reviews);
    }

    /**
     * 인증된 사용자가 작성한 모든 리뷰를 조회합니다.
     *
     * @param userDetails 인증된 사용자 정보
     * @return 사용자가 작성한 리뷰 리스트
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ReviewDto> reviews = reviewServiceV1.getMyReviews(userDetails.getUser());
        return ApiResponse.ok(reviews);
    }

    /**
     * 인증된 사용자가 작성한 특정 가게 리뷰를 조회합니다.
     *
     * @param shopId 조회할 가게의 ID
     * @param userDetails 인증된 사용자 정보
     * @return 사용자가 작성한 해당 가게 리뷰 리스트
     */
    @GetMapping("/me/{shopId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getMyReviewsByShop(
            @PathVariable String shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ReviewDto> reviews = reviewServiceV1.getMyReviewsByShop(userDetails.getUser(), shopId);
        return ApiResponse.ok(reviews);
    }

    /**
     * 리뷰를 수정합니다.
     *
     * <p>
     * 특정 리뷰 ID와 사용자 정보를 기반으로 리뷰를 수정합니다.
     * {@link UpdateReviewRequest}에 포함된 필드만 부분적으로 업데이트되며,
     * 수정된 리뷰 정보는 {@link ReviewDto} 형태로 반환됩니다.
     * </p>
     *
     * @param reviewId 수정할 리뷰 ID
     * @param updateRequest 리뷰 수정 요청 DTO
     * @param userDetails 인증된 사용자 정보({@link UserDetailsImpl})
     * @return 수정된 리뷰 정보를 포함한 {@link ApiResponse} 객체
     * @throws IllegalStateException 리뷰 작성자가 아닌 사용자가 수정 요청을 한 경우
     * @throws IllegalArgumentException 존재하지 않는 리뷰 ID가 전달된 경우
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable String reviewId,
            @Valid @RequestBody UpdateReviewRequest updateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewDto updatedReview = reviewServiceV1.updateReview(userDetails.getUser(), reviewId, updateRequest);
        return ApiResponse.ok(updatedReview);
    }
}
