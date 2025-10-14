package com.sparta.bapzip.review.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.order.application.OrderServiceV1;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.repository.OrderRepository;
import com.sparta.bapzip.review.application.dto.request.CreateReviewRequest;
import com.sparta.bapzip.review.application.exception.DuplicateReviewException;
import com.sparta.bapzip.review.domain.entity.ReviewEntity;
import com.sparta.bapzip.review.domain.repository.ReviewRepository;
import com.sparta.bapzip.review.presentation.dto.response.ReviewCreateResponse;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceV1 {

    private final ReviewRepository reviewRepository;
    private final ShopServiceV1 shopServiceV1;
    private final UserServiceV1 userServiceV1;
    private final OrderServiceV1 orderServiceV1;

    @Transactional
    public ReviewCreateResponse createReview(UserEntity user, CreateReviewRequest request) {

        // 1. 중복 리뷰 확인
        if (reviewRepository.existsByOrderIdAndUserId(request.getOrderId(), user.getId())) {
            throw new DuplicateReviewException(ErrorCode.DUPLICATE_REVIEW);
        }

        // 2. 가게 조회
        ShopEntity shop = shopServiceV1.getShopById(request.getShopId());

        // 3. 주문 조회 (작성 권한 체크 포함)
        OrderEntity order = orderServiceV1.getOrderByIdForReview(request.getOrderId(), user);

        // 4. 리뷰 생성
        ReviewEntity review = ReviewEntity.builder()
                .score(request.getScore())
                .content(request.getContent())
                .shop(shop)
                .user(user)
                .order(order)
                .build();

        ReviewEntity savedReview = reviewRepository.save(review);

        // 5. DTO 변환 후 반환
        return ReviewCreateResponse.from(savedReview);
    }
}
