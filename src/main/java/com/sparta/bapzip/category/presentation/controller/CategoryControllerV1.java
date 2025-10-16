package com.sparta.bapzip.category.presentation.controller;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.exception.CategoryException;
import com.sparta.bapzip.category.presentation.dto.request.CategoryRequestDto;
import com.sparta.bapzip.category.presentation.dto.response.CategoryDetailResponse;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
@Tag(name = "카테고리", description = "카테고리 api")
public class CategoryControllerV1 {
    private final CategoryServiceV1 categoryServiceV1;

    // 삭제되지 않은 카테고리 리스트 조회
    @Operation(summary = "삭제되지 않은 카테고리 리스트 조회", description = "삭제되지 않은 카테고리 리스트 조회 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "삭제되지 않은 카테고리 리스트 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value =
                                                    """
                                                        {
                                                           "success": true,
                                                           "code": 200,
                                                            "data": {
                                                            "content": [
                                                                           {
                                                                              "id": "b1fccda1-7d12-4c2f-b4de-12e7f8e91c22",
                                                                              "name": "카페",
                                                                              "content": "커피, 음료 및 디저트를 제공하는 카페",
                                                                              "deleted": false
                                                                          },
                                                                          {
                                                                              "id": "00000000-0000-0000-0000-000000000001",
                                                                              "name": "한식",
                                                                              "content": "다양한 한국의 맛을 즐길 수 있는 한식 전문점",
                                                                              "deleted": false
                                                                          },
                                                                          {
                                                                              "id": "00000000-0000-0000-0000-000000000002",
                                                                              "name": "중식",
                                                                              "content": "정통 중화요리의 진수를 맛볼 수 있는 중식 전문점",
                                                                              "deleted": false
                                                                          }
                                                                       ]
                                                           }
                                                         }
                                                    """
                                    )
                            }
                    )

            )
    })
    @GetMapping
    public ResponseEntity<List<CategoryDetailResponse>> getActiveCategories() {
        List<CategoryDetailResponse> categories = categoryServiceV1.getActiveCategories();
        return ResponseEntity.ok(categories);
    }

    //  MASTER, MANAGER만 접근 가능
    @Operation(summary = "전체 카테고리 리스트 조회", description = "전체 카테고리 리스트(삭제 포함) 조회 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "전체 카테고리 리스트 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value =
                                                    """
                                                        {
                                                           "success": true,
                                                           "code": 200,
                                                            "data": {
                                                            "content": [
                                                                           {
                                                                               "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                                               "name": "식당",
                                                                               "content": "다양한 음식 메뉴를 제공하는 식당",
                                                                               "deleted": false
                                                                           },
                                                                           {
                                                                               "id": "b1fccda1-7d12-4c2f-b4de-12e7f8e91c22",
                                                                               "name": "카페",
                                                                               "content": "커피, 음료 및 디저트를 제공하는 카페",
                                                                               "deleted": false
                                                                           },
                                                                           {
                                                                               "id": "00000000-0000-0000-0000-000000000001",
                                                                               "name": "한식",
                                                                               "content": "다양한 한국의 맛을 즐길 수 있는 한식 전문점",
                                                                               "deleted": false
                                                                           },
                                                                           {
                                                                               "id": "00000000-0000-0000-0000-000000000002",
                                                                               "name": "중식",
                                                                               "content": "정통 중화요리의 진수를 맛볼 수 있는 중식 전문점",
                                                                               "deleted": false
                                                                           }
                                                                       ]
                                                           }
                                                         }
                                                    """
                                    )
                            }
                    )

            )
    })
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MASTER','MANAGER')")
    public ResponseEntity<List<CategoryDetailResponse>> getAllCategoriesForAdmin() {
        List<CategoryDetailResponse> categories = categoryServiceV1.getAllCategories();
        return ResponseEntity.ok(categories);

    }
    // 카테고리 ID 기준 가게 리스트 조회
    @Operation(summary = "카테고리 ID 기준 가게 리스트 조회", description = "카테고리 ID 기준 가게 리스트 조회 메서드 입니다.")
        @Parameter(name = "page", description = "페이지, Required = false")
        @Parameter(name = "size", description = "한 페이지에 몇 개를 보여줄지, Required = false")
        @Parameter(name = "sortBy", description = "정렬기준(기본적으로는 생성일), Required = false")
        @Parameter(name = "isAsc", description = "오름차순/내림차순, Required = false")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 ID 기준 가게 리스트 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 201,
                                                       "data": {
                                                            "content": [
                                                                {
                                                                    "shopId": "957f86f1-0a97-42b4-9c51-5d30f6a973ba",
                                                                    "name": "강남불고기",
                                                                    "address": "서울시 강남구 역삼동 123-1",
                                                                    "ownerName": "홍길동46",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "e38060e5-7fb7-459b-9c8a-18598347e299",
                                                                    "name": "홍대찌개마을",
                                                                    "address": "서울시 마포구 서교동 456-2",
                                                                    "ownerName": "홍길동47",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "d1a4a9d8-f4d0-4e41-93cd-ad5881674e3c",
                                                                    "name": "화곡족발",
                                                                    "address": "서울시 강서구 화곡동 111-5",
                                                                    "ownerName": "홍길동5",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "5bf988a9-2819-483d-a8ea-17aa4e6cf162",
                                                                    "name": "수원왕갈비",
                                                                    "address": "경기도 수원시 팔달구 인계동 333-7",
                                                                    "ownerName": "홍길동7",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "10ff6f7b-e24a-424c-8721-531986d6a55c",
                                                                    "name": "구월동닭갈비",
                                                                    "address": "인천시 남동구 구월동 444-8",
                                                                    "ownerName": "홍길동8",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "d34e3f80-359c-4251-ba18-84dd620ec097",
                                                                    "name": "동성로찜닭",
                                                                    "address": "대구시 중구 동성로 555-9",
                                                                    "ownerName": "홍길동9",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "0584f46e-8349-4102-a3d7-9e8d810245ed",
                                                                    "name": "둔산칼국수",
                                                                    "address": "대전시 서구 둔산동 777-11",
                                                                    "ownerName": "홍길동11",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "05262677-ca4e-4cc0-8202-3e6c0cc30810",
                                                                    "name": "충장로비빔밥",
                                                                    "address": "광주시 동구 충장로 666-10",
                                                                    "ownerName": "홍길동10",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "8695bcee-3b41-4a5c-ac9c-d4d38942d632",
                                                                    "name": "삼산돼지국밥",
                                                                    "address": "울산시 남구 삼산동 888-12",
                                                                    "ownerName": "홍길동12",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                },
                                                                {
                                                                    "shopId": "2a03a886-e88d-4a10-be49-7a8e567a7a59",
                                                                    "name": "잠실보쌈",
                                                                    "address": "서울시 송파구 잠실동 222-6",
                                                                    "ownerName": "홍길동6",
                                                                    "categoryName": "한식",
                                                                    "avgScore": 0.0
                                                                }
                                                            ],
                                                            "pageable": {
                                                                "pageNumber": 0,
                                                                "pageSize": 10,
                                                                "sort": {
                                                                    "empty": false,
                                                                    "sorted": true,
                                                                    "unsorted": false
                                                                },
                                                                "offset": 0,
                                                                "paged": true,
                                                                "unpaged": false
                                                            },
                                                            "last": false,
                                                            "totalPages": 3,
                                                            "totalElements": 25,
                                                            "size": 10,
                                                            "number": 0,
                                                            "sort": {
                                                                "empty": false,
                                                                "sorted": true,
                                                                "unsorted": false
                                                            },
                                                            "first": true,
                                                            "numberOfElements": 10,
                                                            "empty": false
                                                        }
                                                     }
                                                    """

                                    )
                            }
                    )

            )
    })
    @GetMapping("/{categoryId}/shops")
    @ResponseBody
    public Page<ShopDetailForUserResponse> getShopsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(defaultValue = "false", required = false) boolean isAsc
    ) {
        return categoryServiceV1.getShopsByCategory(categoryId, page, size, sortBy, isAsc);
    } // 카테고리 ID 기준 전체 가게 리스트 조회
    @Operation(summary = "카테고리 ID 기준 전체 가게 리스트 조회", description = "카테고리 ID 기준 전체 가게 리스트(승인되지 않은 가게 포함) 조회 메서드 입니다.")
        @Parameter(name = "page", description = "페이지, Required = false")
        @Parameter(name = "size", description = "한 페이지에 몇 개를 보여줄지, Required = false")
        @Parameter(name = "sortBy", description = "정렬기준(기본적으로는 생성일), Required = false")
        @Parameter(name = "isAsc", description = "오름차순/내림차순, Required = false")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 ID 기준 가게 리스트 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 201,
                                                       "data": {
                                                             "content": [
                                                                 {
                                                                     "shopId": "16d56993-4a3d-400c-abde-7089224cfdb3",
                                                                     "name": "연희반점",
                                                                     "address": "서울시 서대문구 연희동 132-1",
                                                                     "ownerName": "홍길동26",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "255a03f1-8176-416b-acd9-fbe0e5677e55",
                                                                     "name": "인천만다복",
                                                                     "address": "인천시 중구 차이나타운로 45",
                                                                     "ownerName": "홍길동27",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "136a1198-e951-409f-8f6b-41d1d813a4c5",
                                                                     "name": "대치동 홍콩반점",
                                                                     "address": "서울시 강남구 대치동 987-5",
                                                                     "ownerName": "홍길동28",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "077735f6-20e1-4113-b263-24a380be2e38",
                                                                     "name": "부산신발원",
                                                                     "address": "부산시 동구 초량동 1205-2",
                                                                     "ownerName": "홍길동29",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "7ce8a6de-5d5b-4277-8521-50fb5856b821",
                                                                     "name": "연남취향",
                                                                     "address": "서울시 마포구 연남동 228-9",
                                                                     "ownerName": "홍길동30",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "9b17946e-05ed-4737-b45c-60a1af5f89ee",
                                                                     "name": "건대양꼬치거리",
                                                                     "address": "서울시 광진구 자양동 857-1",
                                                                     "ownerName": "홍길동31",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "1b74f8c4-e0d0-4f5a-a228-4f82a0804d99",
                                                                     "name": "평택 동해루",
                                                                     "address": "경기도 평택시 신장동 302-15",
                                                                     "ownerName": "홍길동32",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "d07d65d8-3f7c-411d-a67c-2c0563709158",
                                                                     "name": "삼청동 천진포차",
                                                                     "address": "서울시 종로구 삼청동 122-1",
                                                                     "ownerName": "홍길동33",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "bb7eeeb3-2f46-40d5-8e8b-b7384e75b722",
                                                                     "name": "성내동 딘타이펑",
                                                                     "address": "서울시 강동구 성내동 451-3",
                                                                     "ownerName": "홍길동34",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 },
                                                                 {
                                                                     "shopId": "50ca8f70-666f-4047-b6a9-546a05343fe6",
                                                                     "name": "범어동 리안",
                                                                     "address": "대구시 수성구 범어동 1-10",
                                                                     "ownerName": "홍길동35",
                                                                     "categoryName": "중식",
                                                                     "avgScore": 0.0
                                                                 }
                                                             ],
                                                             "pageable": {
                                                                 "pageNumber": 0,
                                                                 "pageSize": 10,
                                                                 "sort": {
                                                                     "empty": false,
                                                                     "unsorted": false,
                                                                     "sorted": true
                                                                 },
                                                                 "offset": 0,
                                                                 "unpaged": false,
                                                                 "paged": true
                                                             },
                                                             "last": false,
                                                             "totalPages": 2,
                                                             "totalElements": 20,
                                                             "size": 10,
                                                             "number": 0,
                                                             "sort": {
                                                                 "empty": false,
                                                                 "unsorted": false,
                                                                 "sorted": true
                                                             },
                                                             "first": true,
                                                             "numberOfElements": 10,
                                                             "empty": false
                                                       }
                                                     }
                                                    """

                                    )
                            }
                    )

            )
    })
    @GetMapping("/admin/{categoryId}/shops")
    @ResponseBody
    public Page<ShopDetailForUserResponse> getAllShopsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(defaultValue = "false", required = false) boolean isAsc
    ) {
        return categoryServiceV1.getAllShopsByCategory(categoryId, page, size, sortBy, isAsc);
    }

    // 카테고리 생성
    @Operation(summary = "신규 카테고리 등록", description = "신규 카테고리 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "신규 카테고리 등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 201,
                                                       "data": {
                                                          "id": "a6764148-f925-462e-8f2d-5285d394b7b2",
                                                          "name": "일식",
                                                          "content": "정통 일본 요리의 진수를 맛볼 수 있는 일식 전문점",
                                                          "deleted": false
                                                       }
                                                     }
                                                    """
                                    )
                            }
                    )

            )
    })
    @PostMapping("/admin/create")
    public ResponseEntity<CategoryDetailResponse> createCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CategoryRequestDto request) {
        CategoryDetailResponse categoryDetailResponse = categoryServiceV1.createCategory(request.getName(), request.getContent(), userDetails);
        return ResponseEntity.ok(categoryDetailResponse);
    }
    @Operation(summary = "서비스 지역 등록", description = "서비스 지역 등록 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "서비스 지역 등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 201,
                                                       "data": {
                                                            "id": "a6764148-f925-462e-8f2d-5285d394b7b2",
                                                            "name": "일식",
                                                            "content": "일본 현지 요리를 맛볼 수 있는 일식 전문점",
                                                            "deleted": false
                                                        }
                                                     }
                                                    """
                                    )
                            }
                    )

            )
    })
    @PatchMapping("/admin/{categoryId}")
    public ResponseEntity<CategoryDetailResponse> updateCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable UUID categoryId,@RequestBody CategoryRequestDto request) {
        CategoryDetailResponse categoryDetailResponse = categoryServiceV1.updateCategory(categoryId, request.getName(), request.getContent(), userDetails);
        return ResponseEntity.ok(categoryDetailResponse);
    }

    // 카테고리 삭제 (Soft Delete)
    @Operation(summary = "서비스 지역 등록", description = "서비스 지역 등록 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "서비스 지역 등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = CategoryDetailResponse.class),
                            // examples 배열 내부에 예시를 정의
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                       "success": true,
                                                       "code": 201,
                                                       "data": {
                                                            "id": "a6764148-f925-462e-8f2d-5285d394b7b2",
                                                            "name": "일식",
                                                            "content": "일본 현지 요리를 맛볼 수 있는 일식 전문점",
                                                            "deleted": true
                                                        }
                                                     }
                                                    """
                                    )
                            }
                    )

            )
    })
    @DeleteMapping("/admin/{categoryId}")
    public ResponseEntity<CategoryDetailResponse> deleteCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable UUID categoryId) {
        CategoryDetailResponse categoryDetailResponse = categoryServiceV1.deleteCategory(categoryId, userDetails);
        return ResponseEntity.ok(categoryDetailResponse);
    }
}
