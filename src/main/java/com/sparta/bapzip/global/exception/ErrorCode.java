package com.sparta.bapzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Shop 관련 API에서 발생할 수 있는 커스텀 에러
    // Shop: 공통
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 오류가 발생했습니다."),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "가게 소유자를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    SERVICE_AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "서비스 지역을 찾을 수 없습니다."),
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    COORDINATE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "좌표 값이 올바르지 않습니다."),

    // Shop: 수정, 삭제
    UNAUTHORIZED_SHOP_ACCESS(HttpStatus.FORBIDDEN, "해당 가게에 접근할 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
