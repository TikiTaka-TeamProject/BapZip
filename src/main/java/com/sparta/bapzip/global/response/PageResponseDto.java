package com.sparta.bapzip.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "페이징된 리스트 응답 DTO")
public record PageResponseDto<T>(

        @Schema(description = "현재 페이지 번호 (1부터 시작)", example = "1")
        int currentPage,

        @Schema(description = "페이지당 항목 수", example = "10")
        int pageSize,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPages,

        @Schema(description = "전체 데이터 수", example = "50")
        long totalElements,

        @Schema(description = "정렬 기준 필드", example = "createdAt")
        String sortBy,

        @Schema(description = "오름차순 여부(default=false)", example = "false")
        boolean isAsc,

        @Schema(description = "페이지에 포함된 실제 데이터 리스트")
        List<T> items
) {
    public static <T> PageResponseDto<T> fromPage(Page<T> page, String sortBy, boolean isAsc) {
        return new PageResponseDto<>(
                page.getNumber() + 1,   // 1페이지부터
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                sortBy,
                isAsc,
                page.getContent()
        );
    }

    // 기본 fromPage (기본 정렬: createdAt DESC)
    public static <T> PageResponseDto<T> fromPage(Page<T> page) {
        return new PageResponseDto<>(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                "created_at",
                false,
                page.getContent()
        );
    }
}
