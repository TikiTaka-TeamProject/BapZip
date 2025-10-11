package com.sparta.bapzip.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * success 응답
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 데이터 응답에서 제외
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final T data;

    // 생성자
    private ApiResponse(HttpStatus status, T data) {
        this.success = true;
        this.code = status.value();
        this.data = data;
    }

    private ApiResponse(HttpStatus status) {
        this.success = true;
        this.code = status.value();
        this.data = null;
    }

    /**
     * static
     */

    // 데이터 포함 200 ok
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, data));
    }

    // 데이터가 없는 200 ok (noContent)
    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK));
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
