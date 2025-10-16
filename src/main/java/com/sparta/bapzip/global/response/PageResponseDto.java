package com.sparta.bapzip.global.response;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 *
 * @param currentPage 현재 페이지 번호 (1부터 시작)
 * @param pageSize 페이지 데이터 수 (10,30,50 check)
 * @param totalPages 전체 페이지 수
 * @param totalElements 전체 데이터 수
 * @param sortBy 현재 정렬 기준 필드
 * @param isAsc 오름차순(true), false(내림차순 = default)
 * @param items 페이지에 포함되는 실제 데이터
 * @param <T> 페이지에 포함될 데이터 제네릭 타입
 */
public record PageResponseDto<T>(
        int currentPage,
        int pageSize,
        int totalPages,
        long totalElements,
        String sortBy,
        boolean isAsc,
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
