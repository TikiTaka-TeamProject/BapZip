package com.sparta.bapzip.shop.application.dto.request;

import lombok.*;

import java.util.UUID;

/**
 * ShopUpdateRequest
 *
 * 가게(Shop) 수정 시 클라이언트로부터 전달받는 요청 데이터를 담는 DTO 클래스입니다.
 * Controller에서 Service 계층으로 수정할 필드 값을 전달할 때 사용됩니다.
 * 모든 필드는 선택적이며, 변경하고자 하는 값만 포함할 수 있습니다.
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopUpdateRequest {

    /**
     * 가게 이름
     *
     * 수정할 가게의 상호명을 나타냅니다.
     * null일 경우 기존 값이 유지됩니다.
     */
    private String name;

    /**
     * 가게 주소
     *
     * 수정할 가게의 실제 주소를 나타냅니다.
     * 주소가 변경될 경우 Kakao Local API를 통해 좌표 정보도 함께 갱신됩니다.
     * null일 경우 기존 값이 유지됩니다.
     */
    private String address;

    /**
     * 카테고리 ID
     *
     * 가게가 속한 카테고리를 변경할 때 사용됩니다.
     * UUID 형식이며, null일 경우 카테고리는 변경되지 않습니다.
     */
    private UUID categoryId;
}
