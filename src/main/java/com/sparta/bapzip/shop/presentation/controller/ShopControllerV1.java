package com.sparta.bapzip.shop.presentation.controller;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.global.response.PageResponseDto;
import com.sparta.bapzip.servicearea.application.ServiceAreaServiceV1;
import com.sparta.bapzip.shop.application.ShopServiceV1;
import com.sparta.bapzip.shop.application.dto.request.ShopCreationRequest;
import com.sparta.bapzip.shop.application.dto.request.ShopUpdateRequest;
import com.sparta.bapzip.shop.domain.entity.ShopEntity;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.sparta.bapzip.shop.domain.enums.ShopStatusEnum;
import org.springframework.web.bind.annotation.RequestParam;
import com.sparta.bapzip.shop.presentation.dto.response.CreateShopResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/shops")
@RequiredArgsConstructor
public class ShopControllerV1 {

    private final ShopServiceV1 shopServiceV1;
    private final CategoryServiceV1 categoryServiceV1;
    private final ServiceAreaServiceV1 serviceAreaServiceV1;

    /**
     * 새로운 Shop을 생성합니다.
     * POST /v1/shops
     *
     * <p>
     * - 요청자는 반드시 OWNER 권한을 가져야 하며, {@link PreAuthorize} 어노테이션으로 권한 검증이 수행됩니다.
     * - 이미 해당 Owner가 Shop을 가지고 있는 경우 {@link com.sparta.bapzip.shop.application.exception.ShopAlreadyExistsException} 발생
     * - 주소를 기반으로 좌표(location)가 생성되고, 유효성 체크 수행
     *
     * @param shopCreationRequest 새로 생성할 Shop 정보가 담긴 DTO
     * @param userDetails        인증된 사용자 정보(@AuthenticationPrincipal)
     * @return 생성된 Shop 정보를 담은 {@link ApiResponse} 객체
     */
    @Operation(
            summary = "Shop 생성",
            description = "새로운 Shop을 생성합니다. 요청자는 반드시 OWNER 권한을 가져야 합니다."
    )
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<CreateShopResponse>> createShop(
            @RequestBody ShopCreationRequest shopCreationRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ShopEntity savedShop = shopServiceV1.createShop(shopCreationRequest, userDetails.getUser());
        CreateShopResponse response = CreateShopResponse.from(savedShop);

        return ApiResponse.created(response);
    }

    /**
     * 특정 가게 상세 정보 조회 API
     * GET /v1/shops/{shopId}
     *
     * @param shopId 조회할 가게 ID
     * @return ShopDetailResponse: 가게 상세 정보 DTO
     */
    @Operation(summary = "Shop 상세 조회", description = "Shop ID로 상세 정보를 조회합니다.")
    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponse<ShopDetailResponse>> getShopDetail(
            @PathVariable UUID shopId
    ) {
        ShopDetailResponse shopDetailResponse = shopServiceV1.getShopDetail(shopId);
        return ApiResponse.ok(shopDetailResponse);
    }

    /**
     * 특정 Shop의 정보를 수정합니다.
     * PATCH /v1/shops/{shopId}
     *
     * <p>
     * - 수정 가능한 항목: 이름(name), 주소(address), 카테고리(category)
     * - 주소 변경 시 좌표(location) 자동 갱신
     * <p>
     * 요청자는 반드시 OWNER 권한을 가져야 하며, {@link PreAuthorize} 어노테이션으로 권한 검증이 수행됩니다.
     *
     * @param shopId            수정할 Shop의 UUID
     * @param userDetails       인증된 사용자 정보(@AuthenticationPrincipal)
     * @param shopUpdateRequest 수정할 Shop 정보가 담긴 DTO
     * @return 수정된 Shop 정보를 담은 {@link ApiResponse} 객체
     */
    @Operation(summary = "Shop 수정", description = "Shop 정보를 수정합니다. 이름, 주소, 카테고리만 수정 가능합니다.")
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{shopId}")
    public ResponseEntity<ApiResponse<ShopDetailResponse>> updateShop(
            @PathVariable("shopId") UUID shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ShopUpdateRequest shopUpdateRequest

    ) {
        ShopDetailResponse shopDetailResponse = shopServiceV1.updateShop(shopId, userDetails.getUser(), shopUpdateRequest);
        return ApiResponse.ok(shopDetailResponse);
    }


    /**
     * 승인된 가게 리스트 조회
     * GET /v1/shops/
     *
     * <p>
     * ShopServiceV1를 통해 승인 상태(APPROVED)인 가게들을 조회하고,
     * ShopDetailResponse DTO로 변환하여 반환
     * </p>
     *
     * @return  ResponseEntity<List<ShopDetailResponse>> 승인된 가게 리스트
     */
    @Operation(summary = "승인된 Shop 목록 조회", description = "승인 상태(APPROVED)인 Shop 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDto<ShopDetailForUserResponse>>> getApprovedShops(
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", required = false, defaultValue = "false") boolean isAsc
    ) {
        Page<ShopDetailForUserResponse> pageResult = shopServiceV1.getApprovedShops(page, size, sortBy, isAsc);

        return ApiResponse.ok(PageResponseDto.fromPage(pageResult, sortBy, isAsc));
    }

    /**
     * 상태별 가게 목록 조회 API
     * GET /v1/shops/status
     *
     * @param shopStatusEnum 조회할 가게 상태 (ShopStatusEnum), 생략 가능
     *                       - null일 경우 모든 상태의 가게를 조회
     * @return List<ShopDetailResponse> 해당 상태(또는 전체)의 가게 상세 정보 리스트
     */
    @Operation(summary = "Shop 상태별 목록 조회", description = "상태별로 Shop 목록 조회 (MANAGER, MASTER 권한 필요)")
    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
    public ResponseEntity<ApiResponse<PageResponseDto<ShopDetailResponse>>> getShopsByStatus(
            @RequestParam(value = "status", required = false) ShopStatusEnum shopStatusEnum,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", required = false, defaultValue = "false") boolean isAsc
    ) {
        Set<String> allowedSortFields = Set.of("name", "createdAt", "updatedAt");
        sortBy = allowedSortFields.contains(sortBy) ? sortBy : "createdAt";

        Pageable pageable = PageRequest.of(page - 1, size, isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        Page<ShopDetailResponse> pageResult = shopServiceV1.getShopsByStatus(shopStatusEnum, pageable)
                .map(ShopDetailResponse::from);

        return ApiResponse.ok(PageResponseDto.fromPage(pageResult, sortBy, isAsc));
    }

    /**
     * 가게 삭제 API (soft delete)
     * DELETE /v1/shops/{shopId}
     *
     * <p>
     * - 요청한 소유자만 삭제 가능
     * - 실제 삭제는 soft delete 방식(isDeleted = true)
     * - 삭제 후 응답은 HTTP 204 NO CONTENT
     * </p>
     *
     * @param shopId 삭제할 가게 UUID
     * @param userDetails 인증된 사용자 정보
     * @return ResponseEntity<ApiResponse<Void>> 상태 코드 204 반환
     */
    @Operation(summary = "Shop 삭제", description = "Shop을 soft delete 방식으로 삭제 (OWNER 권한 필요)")
    @DeleteMapping("/{shopId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<Void>> deleteShop(
            @PathVariable UUID shopId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long ownerId = userDetails.getUser().getId();
        shopServiceV1.deleteShop(shopId, ownerId);

        return ApiResponse.noContent();
    }

    /**
     * Shop 검색 API
     *
     * <p>
     * 이름, 카테고리, 서비스 지역을 기준으로 Shop을 검색합니다.
     * 페이징 및 정렬 기능을 제공합니다.
     * </p>
     *
     * @param name            검색할 가게 이름 (부분 일치, 선택 사항)
     * @param categoryName    검색할 카테고리 이름 (선택 사항)
     * @param serviceAreaName 검색할 서비스 지역 이름 (선택 사항)
     * @param size            한 페이지에 보여줄 항목 수, 기본값 10
     * @param page            페이지 번호, 기본값 1
     * @param sortBy          정렬 기준 필드, 기본값 "createdAt"
     * @param isAsc           오름차순 여부, 기본값 false (내림차순)
     * @return ResponseEntity<ApiResponse<PageResponseDto<ShopDetailForUserResponse>>>
     *         검색 조건에 맞는 Shop의 페이징 결과를 반환
     */
    @Operation(summary = "Shop 검색", description = "이름, 카테고리, 서비스 지역으로 Shop 검색")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponseDto<ShopDetailForUserResponse>>> searchShops(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "serviceAreaName", required = false) String serviceAreaName,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", required = false, defaultValue = "false") boolean isAsc
    ) {
        UUID categoryId = null;
        if (categoryName != null) {
            categoryId = categoryServiceV1.getCategoryIdByName(categoryName);
        }

        Polygon areaPolygon = null;
        if (serviceAreaName != null) {
            areaPolygon = serviceAreaServiceV1.getServiceAreaPolygonByName(serviceAreaName);
        }

        Page<ShopDetailForUserResponse> dtoPage = shopServiceV1.searchShops(name, categoryId, areaPolygon, page, size, sortBy, isAsc);

        return ApiResponse.ok(PageResponseDto.fromPage(dtoPage, sortBy, isAsc));
    }
}

