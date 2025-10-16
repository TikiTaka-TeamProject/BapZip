package com.sparta.bapzip.category.presentation.dto.response;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "카테고리 요청 응답형식")
public class CategoryDetailResponse {

    @Schema(description = "카테고리 식별자", example = "7294b430-57f8-467b-b6c1-09a3ceae3186")
    private UUID id;

    @Schema(description = "카테고리 이름", example = "한식")
    private String name;

    @Schema(description = "카테고리 설명", example = "다양한 한국의 맛을 즐길 수 있는 한식 전문점")
    private String content;

    @Schema(description = "카테고리 활성화 상태", example = "true")
    private boolean isDeleted;

    public static CategoryDetailResponse toDtoForAdmin(CategoryEntity category) {
        return CategoryDetailResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .content(category.getContent())
                .isDeleted(category.getIsDeleted())
                .build();
    }

    public static CategoryDetailResponse toDto(CategoryEntity category) {
        return CategoryDetailResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .content(category.getContent())
                .build();
    }

}
