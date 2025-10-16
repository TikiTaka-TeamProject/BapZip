package com.sparta.bapzip.category.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "카테고리 요청 형식")
public class CategoryRequestDto {

    @Schema(description = "카테고리 식별자", example = "7294b430-57f8-467b-b6c1-09a3ceae3186")
    private UUID id;

    @Schema(description = "카테고리 이름", example = "한식")
    private String name;

    @Schema(description = "카테고리 설명", example = "다양한 한국의 맛을 즐길 수 있는 한식 전문점")
    private String content;
}
