package com.sparta.bapzip.global.common.dto;
import org.springframework.data.domain.Page;
import java.util.List;

public record PageResponse<T>(
        int currentPage,    // 현재 페이지 번호
        int pageSize,       // 현재 페이지 데이터 수 (10,30,50 check)
        int totalPages,     // 전체 페이지 수
        long totalElements, // 전체 데이터 수
        String sortBy,      // 현재 정렬 기준 필드
        boolean isAsc,      // 오름차순(true), false(내림차순 = default)
        List<T> items       // 페이지에 포함되는 실제 데이터
) {
    public static <T> PageResponse<T> fromPage(Page<T> page, String sortBy, boolean isAsc) {
        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                sortBy,
                isAsc,
                page.getContent()
        );
    }
}
