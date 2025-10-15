package com.sparta.bapzip.review.application;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.presentation.dto.request.CategoryRequestDto;
import com.sparta.bapzip.order.application.OrderServiceV1;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.order.domain.exception.ForbiddenOrderAccessException;
import com.sparta.bapzip.order.domain.exception.OrderNotDeliveredException;
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
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewServiceV1 테스트")
class ReviewServiceV1Test {
    @InjectMocks
    private ReviewServiceV1 reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ShopServiceV1 shopServiceV1;

    @Mock
    private OrderServiceV1 orderServiceV1;

    private UserEntity customer1;
    private UserEntity customer2;
    private UserEntity owner;
    private ShopEntity shop;
    private OrderEntity deliveredOrder;
    private OrderEntity deliveringOrder;
    private ReviewEntity review;
    private CreateReviewRequest createReviewRequest;
    private UpdateReviewRequest updateReviewRequest;

    @BeforeEach
    void setUp() {
        // 고객 설정
        customer1 = UserEntity.builder()
                .id(1L)
                .name("고객1")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        customer2 = UserEntity.builder()
                .id(2L)
                .name("고객2")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        // 가게 주인 설정
        owner = UserEntity.builder()
                .id(3L)
                .name("사장님")
                .role(UserRoleEnum.OWNER)
                .build();

        // 가게 설정
        shop = ShopEntity.builder()
                .id(UUID.randomUUID())
                .name("테스트 가게")
                .owner(owner)
                .build();

        // 배달 완료된 주문 설정
        deliveredOrder = OrderEntity.builder()
                .id(UUID.randomUUID())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .status(OrderStatus.DELIVERED)
                .user(customer1)
                .build();

        // 배달 중인 주문 설정
        deliveringOrder = OrderEntity.builder()
                .id(UUID.randomUUID())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .status(OrderStatus.DELIVERING)
                .user(customer1)
                .build();

        // 리뷰 생성 요청 설정
        createReviewRequest = CreateReviewRequest.builder()
                .shopId(shop.getId())
                .orderId(deliveredOrder.getId())
                .score(5)
                .content("정말 맛있어요!")
                .build();

        // 리뷰 수정 요청 설정
        updateReviewRequest = UpdateReviewRequest.builder()
                .score(4)
                .content("수정된 리뷰입니다")
                .build();

        // 리뷰 엔티티 설정
        review = ReviewEntity.builder()
                .id(UUID.randomUUID())
                .user(customer1)
                .shop(shop)
                .order(deliveredOrder)
                .content("맛있어요!")
                .score(5)
                .build();
    }

    @Nested
    @DisplayName("리뷰 생성 테스트")
    class CreateReviewTests {

        @Test
        @DisplayName("리뷰 생성 성공")
        void createReviewSuccess() {
            // given
            when(reviewRepository.existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId())).thenReturn(false);
            when(shopServiceV1.getShopById(shop.getId())).thenReturn(shop);
            when(orderServiceV1.getOrderByIdForReview(deliveredOrder.getId(), customer1)).thenReturn(deliveredOrder);
            when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

            // when
            ReviewCreateResponse result = reviewService.createReview(customer1, createReviewRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEqualTo(review.getContent());
            assertThat(result.getScore()).isEqualTo(review.getScore());
            verify(reviewRepository, times(1)).existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId());
            verify(shopServiceV1, times(1)).getShopById(shop.getId());
            verify(orderServiceV1, times(1)).getOrderByIdForReview(deliveredOrder.getId(), customer1);
            verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
        }

        @Test
        @DisplayName("중복 리뷰 생성 실패")
        void createReviewDuplicateReview() {
            // given
            when(reviewRepository.existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> reviewService.createReview(customer1, createReviewRequest))
                    .isInstanceOf(DuplicateReviewException.class);
            verify(reviewRepository, times(1)).existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId());
            verify(shopServiceV1, never()).getShopById(any());
            verify(orderServiceV1, never()).getOrderByIdForReview(any(), any());
            verify(reviewRepository, never()).save(any(ReviewEntity.class));
        }

        @Test
        @DisplayName("가게 조회 후 리뷰 생성")
        void createReviewWithShopValidation() {
            // given
            when(reviewRepository.existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId())).thenReturn(false);
            when(shopServiceV1.getShopById(shop.getId())).thenReturn(shop);
            when(orderServiceV1.getOrderByIdForReview(deliveredOrder.getId(), customer1)).thenReturn(deliveredOrder);
            when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

            // when
            ReviewCreateResponse result = reviewService.createReview(customer1, createReviewRequest);

            // then
            assertThat(result).isNotNull();
            verify(shopServiceV1, times(1)).getShopById(shop.getId());
        }

        @Test
        @DisplayName("주문 조회 및 권한 체크 후 리뷰 생성")
        void createReviewWithOrderValidation() {
            // given
            when(reviewRepository.existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId())).thenReturn(false);
            when(shopServiceV1.getShopById(shop.getId())).thenReturn(shop);
            when(orderServiceV1.getOrderByIdForReview(deliveredOrder.getId(), customer1)).thenReturn(deliveredOrder);
            when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

            // when
            ReviewCreateResponse result = reviewService.createReview(customer1, createReviewRequest);

            // then
            assertThat(result).isNotNull();
            verify(orderServiceV1, times(1)).getOrderByIdForReview(deliveredOrder.getId(), customer1);
        }
    }

    @Nested
    @DisplayName("리뷰 조회 테스트")
    class GetReviewTests {

        @Test
        @DisplayName("가게의 모든 리뷰 조회 성공")
        void getReviewsByShopSuccess() {
            // given
            List<ReviewEntity> reviews = Arrays.asList(review);
            when(reviewRepository.findAllByShopIdAndIsDeletedFalse(shop.getId())).thenReturn(reviews);

            // when
            List<ReviewDto> result = reviewService.getReviewsByShop(shop.getId().toString());

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getContent()).isEqualTo(review.getContent());
            verify(reviewRepository, times(1)).findAllByShopIdAndIsDeletedFalse(shop.getId());
        }

        @Test
        @DisplayName("사용자의 모든 리뷰 조회 성공")
        void getMyReviewsSuccess() {
            // given
            List<ReviewEntity> reviews = Arrays.asList(review);
            when(reviewRepository.findAllByUserIdAndIsDeletedFalse(customer1.getId())).thenReturn(reviews);

            // when
            List<ReviewDto> result = reviewService.getMyReviews(customer1);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getContent()).isEqualTo(review.getContent());
            verify(reviewRepository, times(1)).findAllByUserIdAndIsDeletedFalse(customer1.getId());
        }
        @Test
        @DisplayName("사용자의 특정 가게 리뷰 조회 성공")
        void getMyReviewsByShopSuccess() {
            // given
            List<ReviewEntity> reviews = Arrays.asList(review);
            when(reviewRepository.findAllByUserIdAndShopIdAndIsDeletedFalse(customer1.getId(), shop.getId())).thenReturn(reviews);

            // when
            List<ReviewDto> result = reviewService.getMyReviewsByShop(customer1, shop.getId().toString());
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getContent()).isEqualTo(review.getContent());
            verify(reviewRepository, times(1)).findAllByUserIdAndShopIdAndIsDeletedFalse(customer1.getId(), shop.getId());

        }

        @Test
        @DisplayName("가게에 리뷰가 없는 경우 빈 리스트 반환")
        void getReviewsByShopEmptyList() {
            // given
            when(reviewRepository.findAllByShopIdAndIsDeletedFalse(shop.getId())).thenReturn(Collections.emptyList());

            // when
            List<ReviewDto> result = reviewService.getReviewsByShop(shop.getId().toString());

            // then
            assertThat(result).isEmpty();
            verify(reviewRepository, times(1)).findAllByShopIdAndIsDeletedFalse(shop.getId());
        }
    }

    @Nested
    @DisplayName("리뷰 수정 테스트")
    class UpdateReviewTests {

        @Test
        @DisplayName("리뷰 수정 성공")
        void updateReviewSuccess() {
            // given
            String reviewId = review.getId().toString();
            when(reviewRepository.findById(UUID.fromString(reviewId))).thenReturn(Optional.of(review));

            // when
            ReviewDto result = reviewService.updateReview(customer1, reviewId, updateReviewRequest);

            // then
            assertThat(result).isNotNull();
            verify(reviewRepository, times(1)).findById(UUID.fromString(reviewId));
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 수정 실패")
        void updateReviewNotFound() {
            // given
            String reviewId = UUID.randomUUID().toString();
            when(reviewRepository.findById(UUID.fromString(reviewId))).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(customer1, reviewId, updateReviewRequest))
                    .isInstanceOf(ReviewNotFoundException.class);
            verify(reviewRepository, times(1)).findById(UUID.fromString(reviewId));
        }

        @Test
        @DisplayName("다른 사용자의 리뷰 수정 실패 - 권한 없음")
        void updateReviewUnauthorizedAccess() {
            // given
            String reviewId = review.getId().toString();
            when(reviewRepository.findById(UUID.fromString(reviewId))).thenReturn(Optional.of(review));

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(customer2, reviewId, updateReviewRequest))
                    .isInstanceOf(UnauthorizedReviewAccessException.class);
            verify(reviewRepository, times(1)).findById(UUID.fromString(reviewId));
        }

        @Test
        @DisplayName("리뷰 수정 시 작성자 검증")
        void updateReviewValidateAuthor() {
            // given
            String reviewId = review.getId().toString();
            when(reviewRepository.findById(UUID.fromString(reviewId))).thenReturn(Optional.of(review));

            // when
            ReviewDto result = reviewService.updateReview(customer1, reviewId, updateReviewRequest);

            // then
            assertThat(result).isNotNull();
            verify(reviewRepository, times(1)).findById(UUID.fromString(reviewId));
            // 작성자가 맞으므로 예외가 발생하지 않고 정상적으로 수정됨
        }
    }

    @Nested
    @DisplayName("리뷰 DTO 변환 테스트")
    class ReviewDtoConversionTests {

        @Test
        @DisplayName("ReviewEntity를 ReviewDto로 변환")
        void convertToDtoSuccess() {
            // given
            List<ReviewEntity> reviews = Arrays.asList(review);
            when(reviewRepository.findAllByUserIdAndIsDeletedFalse(customer1.getId())).thenReturn(reviews);

            // when
            List<ReviewDto> result = reviewService.getMyReviews(customer1);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            ReviewDto dto = result.get(0);
            assertThat(dto.getContent()).isEqualTo(review.getContent());
            assertThat(dto.getScore()).isEqualTo(review.getScore());
        }

        @Test
        @DisplayName("여러 ReviewEntity를 ReviewDto 리스트로 변환")
        void convertToDtoListSuccess() {
            // given
            ReviewEntity review2 = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer1)
                    .shop(shop)
                    .order(deliveredOrder)
                    .content("두 번째 리뷰")
                    .score(4)
                    .build();

            List<ReviewEntity> reviews = Arrays.asList(review, review2);
            when(reviewRepository.findAllByShopIdAndIsDeletedFalse(shop.getId())).thenReturn(reviews);

            // when
            List<ReviewDto> result = reviewService.getReviewsByShop(shop.getId().toString());

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getContent()).isEqualTo(review.getContent());
            assertThat(result.get(1).getContent()).isEqualTo(review2.getContent());
        }
    }

    @Nested
    @DisplayName("리뷰 생성 플로우 통합 테스트")
    class ReviewCreationFlowTests {

        @Test
        @DisplayName("리뷰 생성 전체 플로우 검증")
        void createReviewFullFlow() {
            // given
            when(reviewRepository.existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId())).thenReturn(false);
            when(shopServiceV1.getShopById(shop.getId())).thenReturn(shop);
            when(orderServiceV1.getOrderByIdForReview(deliveredOrder.getId(), customer1)).thenReturn(deliveredOrder);
            when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

            // when
            ReviewCreateResponse result = reviewService.createReview(customer1, createReviewRequest);

            // then
            // 1. 중복 체크
            verify(reviewRepository).existsByOrderIdAndUserId(deliveredOrder.getId(), customer1.getId());
            // 2. 가게 조회
            verify(shopServiceV1).getShopById(shop.getId());
            // 3. 주문 조회 및 권한 체크
            verify(orderServiceV1).getOrderByIdForReview(deliveredOrder.getId(), customer1);
            // 4. 리뷰 저장
            verify(reviewRepository).save(any(ReviewEntity.class));
            // 5. 결과 확인
            assertThat(result).isNotNull();
        }
    }
}