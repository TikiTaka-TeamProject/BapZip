package com.sparta.bapzip.shop.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


/**
 * ShopCreationRequest
 *
 * 가게(Shop) 생성 시 클라이언트로부터 전달받는 요청 데이터를 담는 DTO 클래스입니다.
 * Controller에서 Service 계층으로 데이터를 전달할 때 사용됩니다.
 * 각 필드는 @NotNull 어노테이션으로 필수 값 검증이 적용되어 있습니다.
 */
@Builder
@Getter
public class ShopCreationRequest {

    /**
     * 가게 이름
     *
     * 가게의 상호명을 나타냅니다.
     * Null일 수 없으며 필수 값입니다.
     */
    @NotNull(message = "가게 이름은 필수입니다.")
    private String name;

    /**
     * 가게 주소
     *
     * 가게의 실제 주소를 나타냅니다.
     * Kakao Local API를 통해 좌표 정보를 조회할 때 사용됩니다.
     * Null일 수 없으며 필수 값입니다.
     */
    @NotNull(message = "가게 주소는 필수입니다.")
    private String address;

    /**
     * 오너 ID
     *
     * 가게를 소유한 사용자의 식별자입니다.
     * 사용자 권한 검증에 사용됩니다.
     * Null일 수 없으며 필수 값입니다.
     */
    @NotNull(message = "오너 ID는 필수입니다.")
    private Long ownerId;

    /**
     * 카테고리 ID
     *
     * 가게가 속한 카테고리의 식별자입니다.
     * UUID 형식이며, 카테고리 유효성 검증 및 ShopEntity와의 연관관계 설정에 사용됩니다.
     * Null일 수 없으며 필수 값입니다.
     */
    @NotNull(message = "카테고리 ID는 필수입니다.")
    private UUID categoryId;
}