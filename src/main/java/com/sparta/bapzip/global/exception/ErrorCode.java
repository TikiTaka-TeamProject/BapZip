package com.sparta.bapzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Payment:결제 정보 조회 관련
    PAYMENT_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "결제 요청에 실패했습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.METHOD_NOT_ALLOWED, "결제 취소 할 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."),
    PAYMENT_KEY_MISSING(HttpStatus.BAD_REQUEST, "결제 키가 누락되었습니다."),
    PAYMENT_CANCELLATION_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "결제 취소 할 수 없습니다."),
    
    // CATEGORY 관련 에러
    DUPLICATE_CATEGORY_EXCEPTION(HttpStatus.CONFLICT, "중복 된 카테고리입니다."),
    INVALID_CATEGORY_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 ID입니다."),
    // USER 관련 에러
    DUPLICATE_USER_EXCEPTION(HttpStatus.CONFLICT, "중복 된 유저입니다."),
    UNAUTHORIZED_USER_EXCEPTION(HttpStatus.UNAUTHORIZED, "권한이 없는 유저입니다."),
    PASSWORD_NOT_MATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다."),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),

    // MENU todo: 상태코드 별 분리 필요
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 메뉴입니다."),
    INVALID_MENU_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 메뉴가 포함되어 있습니다."),


    
    //404 Not Found
    AI_LOG_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 Ai-Log입니다."),
    KAKAO_MAP_DOCUMENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "query에 대한 응답 documents가 없습니다."),
    KAKAO_MAP_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "query에 대한 응답 address가 없습니다."),


    // ORDER
    MENU_NOT_IN_SHOP(HttpStatus.BAD_REQUEST, "다른 가게의 메뉴가 포함되어 있습니다."),
    MENU_NOT_FOUNT_IN_ORDER(HttpStatus.BAD_REQUEST, "존재하지 않는 메뉴가 포함되어 있습니다."),
    SOLD_OUT_MENU(HttpStatus.BAD_REQUEST, "선택하신 메뉴 중 품절된 메뉴가 포함되어 있습니다."),
    FORBIDDEN_ORDER_ACCESS(HttpStatus.FORBIDDEN, "본인의 주문만 조회할 수 있습니다.");
  
    // ====================== Shop ======================
    // Shop 관련 API에서 발생할 수 있는 커스텀 에러
    // Shop: 공통
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 오류가 발생했습니다."),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "가게 소유자를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    SERVICE_AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "서비스 지역을 찾을 수 없습니다."),
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    COORDINATE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "좌표 값이 올바르지 않습니다."),

    // Shop: 생성
    SHOP_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 소유자는 이미 가게를 보유하고 있습니다."),

    // Shop: 수정, 삭제
    UNAUTHORIZED_SHOP_ACCESS(HttpStatus.FORBIDDEN, "해당 가게에 접근할 권한이 없습니다."),
    SHOP_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 가게를 삭제할 권한이 없습니다."),
    SHOP_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 삭제된 가게입니다."),
    SHOP_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "가게 삭제 처리 중 오류가 발생했습니다."),
    SHOP_HAS_ACTIVE_ORDERS(HttpStatus.BAD_REQUEST, "진행 중인 주문이 있는 가게는 삭제할 수 없습니다."),

    

    private final HttpStatus status;
    private final String message;
}
