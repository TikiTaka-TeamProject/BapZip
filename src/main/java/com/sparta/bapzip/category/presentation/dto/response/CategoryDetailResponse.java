package com.sparta.bapzip.category.presentation.dto.response;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailResponse {

    private UUID id;

    private String name;

    private String content;

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
