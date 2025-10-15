package com.sparta.bapzip.review.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.order.application.OrderServiceV1;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.review.application.dto.ReviewDto;
import com.sparta.bapzip.review.application.dto.request.CreateReviewRequest;
import com.sparta.bapzip.review.application.dto.request.UpdateReviewRequest;
import com.sparta.bapzip.review.application.exception.DuplicateReviewException;
import com.sparta.bapzip.review.application.exception.ReviewNotFoundException;
import com.sparta.bapzip.review.application.exception.UnauthorizedReviewAccessException;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 리뷰 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * <p>리뷰 작성, 중복 리뷰 체크, 가게 및 주문 조회 등의 기능을 수행합니다.</p>
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceV1 {

    private final ReviewRepository reviewRepository;
    private final ShopServiceV1 shopServiceV1;
    private final OrderServiceV1 orderServiceV1;

    /**
     * 사용자가 작성한 리뷰를 생성합니다.
     *
     * <p>1. 주문과 사용자 기준으로 중복 리뷰 확인<br>
     * 2. 가게 조회<br>
     * 3. 주문 조회 및 작성 권한 확인<br>
     * 4. 리뷰 생성 및 저장<br>
     * 5. 저장된 리뷰를 DTO로 변환 후 반환</p>
     *
     * @param user    리뷰를 작성하는 사용자
     * @param request 리뷰 작성 요청 DTO
     * @return 생성된 리뷰 정보를 담은 {@link ReviewCreateResponse} 객체
     * @throws DuplicateReviewException 동일 주문에 대해 이미 작성된 리뷰가 존재할 경우 발생
     */
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

    /**
     * 특정 가게에 작성된 모든 리뷰를 조회합니다.
     *
     * @param shopId 조회할 가게의 ID
     * @return 조회된 리뷰 리스트를 {@link ReviewDto} 형태로 반환
     */
    public List<ReviewDto> getReviewsByShop(String shopId) {
        List<ReviewEntity> reviews = reviewRepository.findAllByShopIdAndIsDeletedFalse(UUID.fromString(shopId));
        return reviews.stream().map(ReviewDto::from).collect(Collectors.toList());
    }

    /**
     * 특정 사용자가 작성한 모든 리뷰를 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 조회된 리뷰 리스트를 {@link ReviewDto} 형태로 반환
     */
    public List<ReviewDto> getMyReviews(UserEntity user) {
        List<ReviewEntity> reviews = reviewRepository.findAllByUserIdAndIsDeletedFalse(user.getId());
        return reviews.stream().map(ReviewDto::from).collect(Collectors.toList());
    }

    /**
     * 특정 사용자가 작성한 특정 가게 리뷰를 조회합니다.
     *
     * @param user   조회할 사용자
     * @param shopId 조회할 가게의 ID
     * @return 조회된 리뷰 리스트를 {@link ReviewDto} 형태로 반환
     */
    public List<ReviewDto> getMyReviewsByShop(UserEntity user, String shopId) {
        List<ReviewEntity> reviews = reviewRepository.findAllByUserIdAndShopIdAndIsDeletedFalse(user.getId(), UUID.fromString(shopId));
        return reviews.stream().map(ReviewDto::from).collect(Collectors.toList());
    }


    /**
     * 리뷰를 부분 수정(PATCH)합니다.
     *
     * <p>
     * 지정된 리뷰 ID와 사용자 정보를 기반으로 리뷰를 수정합니다.
     * {@link UpdateReviewRequest}의 필드(score, content)만 업데이트하며,
     * 리뷰 작성자가 아닌 경우 수정할 수 없습니다.
     * 수정 후 {@link ReviewDto} 형태로 반환합니다.
     * </p>
     *
     * @param user       리뷰 작성자 정보
     * @param reviewId   수정할 리뷰 ID
     * @param request    리뷰 수정 요청 DTO
     * @return 수정된 리뷰 정보를 담은 {@link ReviewDto}
     * @throws IllegalStateException    리뷰 작성자가 아닌 사용자가 수정 요청을 한 경우
     * @throws IllegalArgumentException 존재하지 않는 리뷰 ID가 전달된 경우
     */
    @Transactional
    public ReviewDto updateReview(UserEntity user, String reviewId, UpdateReviewRequest request) {
        ReviewEntity review = reviewRepository.findById(UUID.fromString(reviewId))
                .orElseThrow(() -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 체크
        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedReviewAccessException(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);
        }

        // 리뷰 수정
        review.updateReview(request.getScore(), request.getContent());

        return ReviewDto.from(review);
    }

    /**
     * 사용자가 작성한 리뷰를 삭제(soft delete)합니다.
     *
     * <p>
     * 리뷰 ID와 사용자 정보를 기반으로 해당 리뷰를 조회하고,
     * 작성자 본인 여부를 검증한 뒤 soft delete 처리를 수행합니다.
     * 삭제 시 실제 데이터를 물리적으로 제거하지 않고 {@code deletedAt}, {@code deletedBy}, {@code isDeleted} 값을 갱신합니다.
     * </p>
     *
     * @param user     삭제를 요청한 사용자 정보
     * @param reviewId 삭제할 리뷰의 고유 ID
     * @throws ReviewNotFoundException            존재하지 않는 리뷰 ID일 경우 발생
     * @throws UnauthorizedReviewAccessException  리뷰 작성자가 아닌 사용자가 삭제 요청을 한 경우 발생
     */
    @Transactional
    public void deleteReview(UserEntity user, UUID reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        // 작성자 체크
        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedReviewAccessException(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);
        }

        // soft delete
        review.markDeleted(user.getId());
        reviewRepository.save(review);
    }
}
