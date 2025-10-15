package com.sparta.bapzip.global.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int[] ALLOWED_SIZES = {10, 30, 50};

    /**
     * 허용된 페이지 사이즈인지 체크
     */
    private static int validatePageSize(int size) {
        for (int allowed : ALLOWED_SIZES) {
            if (size == allowed) {
                return size;
            }
        }
        return DEFAULT_PAGE_SIZE;
    }

    /**
     * Pageable 생성 (기본 정렬: createdAt 내림차순)
     */
    public static Pageable createDefaultPageable(int page, int size) {
        int pageSize = validatePageSize(size);
        return PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Pageable 생성 (외부에서 정렬 기준 및 방향 지정 가능)
     */
    public static Pageable createPageable(int page, int size, String sortBy, boolean isAsc) {
        int pageSize = validatePageSize(size);
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by(direction, sortBy));
    }
}
