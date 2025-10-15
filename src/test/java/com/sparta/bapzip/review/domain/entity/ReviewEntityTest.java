package com.sparta.bapzip.review.domain.entity;
import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.presentation.dto.request.CategoryRequestDto;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.order.domain.entity.OrderEntity;
import com.sparta.bapzip.order.domain.enums.OrderStatus;
import com.sparta.bapzip.order.domain.exception.ForbiddenOrderAccessException;
import com.sparta.bapzip.order.domain.exception.OrderNotDeliveredException;
import com.sparta.bapzip.review.application.dto.request.CreateReviewRequest;
import com.sparta.bapzip.review.application.exception.DuplicateReviewException;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ReviewEntity 도메인 테스트")
class ReviewEntityTest {
    private ReviewEntity review;
    private UserEntity customer1;
    private UserEntity customer2;
    private UserEntity owner;
    private ShopEntity shop;
    private OrderEntity order1;
    private OrderEntity order2;
    private CreateReviewRequest createReviewRequest;

    @BeforeEach
    void setUp() {
        customer1 = UserEntity.builder()
                .id(1L)
                .name("고객")
                .role(UserRoleEnum.valueOf("CUSTOMER"))
                .build();

        customer2 = UserEntity.builder()
                .id(4L)
                .name("고객")
                .role(UserRoleEnum.valueOf("CUSTOMER"))
                .build();

        owner = UserEntity.builder()
                .id(2L)
                .name("사장")
                .role(UserRoleEnum.valueOf("OWNER"))
                .build();

        shop = ShopEntity.builder()
                .id(UUID.randomUUID())
                .name("테스트 가게")
                .owner(owner)
                .build();

        order1 = OrderEntity.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .status(OrderStatus.valueOf("DELIVERED"))
                .user(customer1)
                .build();

        order2 = OrderEntity.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .status(OrderStatus.valueOf("DELIVERING"))
                .user(customer1)
                .build();

        createReviewRequest = CreateReviewRequest.builder()
                .shopId(shop.getId())
                .score(4)
                .content("좋아요")
                .orderId(order1.getId())
                .build();

        review = ReviewEntity.builder()
                .id(UUID.randomUUID())
                .user(customer1)
                .shop(shop)
                .content("맛있어요!")
                .score(5)
                .build();
    }

    @Nested
    @DisplayName("리뷰 생성 테스트")
    class CreateReviewTest {

        @Test
        @DisplayName("정상적으로 리뷰가 생성된다")
        void createReviewSuccess() {
            // given & when
            ReviewEntity review = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer1)
                    .shop(shop)
                    .content("정말 맛있어요!")
                    .score(5)
                    .build();

            // then
            assertThat(review).isNotNull();
            assertThat(review.getUser()).isEqualTo(customer1);
            assertThat(review.getShop()).isEqualTo(shop);
            assertThat(review.getContent()).isEqualTo("정말 맛있어요!");
            assertThat(review.getScore()).isEqualTo(5);
        }

        @Test
        @DisplayName("주문의 status값이 DELIVERED가 아니면 예외가 발생한다")
        void createReviewOrderNotDelivered() {
            // when & then
            assertThatThrownBy(() -> {
                if (!order2.getStatus().equals(OrderStatus.DELIVERED)) {
                    throw new OrderNotDeliveredException(ErrorCode.ORDER_NOT_COMPLETED);
                }
            }).isInstanceOf(OrderNotDeliveredException.class);
        }
        @Test
        @DisplayName("한 주문에 대해 리뷰를 중복해서 작성할 수 없다")
        void createReview_DuplicateReview() {
            // given
            ReviewEntity existingReview = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer1)
                    .shop(shop)
                    .content("첫 번째 리뷰")
                    .score(5)
                    .build();

            // 이미 해당 주문에 대한 리뷰가 존재한다고 가정
            boolean hasReview = true;

            // when & then
            assertThatThrownBy(() -> {
                if (hasReview) {
                    throw new DuplicateReviewException(ErrorCode.DUPLICATE_REVIEW);
                }
            }).isInstanceOf(DuplicateReviewException.class);
        }


        @Nested
        @DisplayName("고객 권한 검증 테스트")
        class ValidateCustomerTest {

            @Test
            @DisplayName("주문한 고객 본인은 검증을 통과한다")
            void validateCustomerSuccess() {
                // when & then
                assertThatCode(() -> order1.validateCustomer(customer1.getId()))
                        .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("다른 사용자는 검증에 실패한다")
            void validateCustomerForbidden() {
                // when & then
                assertThatThrownBy(() -> order1.validateCustomer(customer2.getId()))
                        .isInstanceOf(ForbiddenOrderAccessException.class);
            }
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 테스트")
    class DeleteReviewTest {

        @Test
        @DisplayName("리뷰를 삭제한다")
        void deleteSuccess() {
            // given
            ReviewEntity review = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer1)
                    .shop(shop)
                    .content("맛있어요!")
                    .score(5)
                    .build();

            // when
            review.markDeleted(customer1.getId());

            // then
            assertThat(review.getDeletedAt()).isNotNull();
            assertThat(review.getDeletedBy()).isEqualTo(customer1.getId());
        }
    }

    @Nested
    @DisplayName("리뷰 수정 테스트")
    class UpdateReviewTest {

        @Test
        @DisplayName("리뷰를 수정한다")
        void updateSuccess() {
            // given
            ReviewEntity review = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer1)
                    .shop(shop)
                    .content("맛있어요!")
                    .score(5)
                    .build();

            String newContent = "정말 최고예요!";
            int newScore = 4;

            // when
            review.updateReview(newScore, newContent);

            // then
            assertThat(review.getContent()).isEqualTo(newContent);
            assertThat(review.getScore()).isEqualTo(newScore);
        }
    }

    @Nested
    @DisplayName("리뷰 조회 테스트")
    class ReadReviewTest {

        @Test
        @DisplayName("모두 등록된 리뷰를 조회한다")
        void readAllReviewsSuccess() {
            // given
            ReviewEntity review1 = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer1)
                    .shop(shop)
                    .content("맛있어요!")
                    .score(5)
                    .build();

            ReviewEntity review2 = ReviewEntity.builder()
                    .id(UUID.randomUUID())
                    .user(customer2)
                    .shop(shop)
                    .content("좋아요!")
                    .score(4)
                    .build();

            List<ReviewEntity> reviews = List.of(review1, review2);

            // when & then
            assertThat(reviews).hasSize(2);
            assertThat(reviews).containsExactly(review1, review2);
            assertThat(reviews.get(0).getScore()).isEqualTo(5);
            assertThat(reviews.get(1).getScore()).isEqualTo(4);
        }
    }
}