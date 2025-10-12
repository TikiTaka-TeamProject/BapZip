package com.sparta.bapzip.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * success 응답
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // null 데이터 응답에서 제외
public record ApiResponse<T>(
        boolean success,
        int code,
        T data
) {

    public ApiResponse(HttpStatus status, T data) {
        this(true, status.value(), data);
    }

    public ApiResponse(HttpStatus status) {
        this(true, status.value(), null);
    }

    /**
     * static
     */

    // 200 OK
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK));
    }

    // 201 CREATED
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED, data));
    }

    // 204 No Content
    public static <T> ResponseEntity<ApiResponse<T>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(HttpStatus.NO_CONTENT));
    }


    // HTTP 상태 지정 성공 응답 of (데이터 O)
    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, data));
    }

    // HTTP 상태 지정 성공 응답 of (데이터 X)
    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status));
    }
}
