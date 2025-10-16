package com.sparta.bapzip.category.presentation.controller;

import com.sparta.bapzip.category.application.CategoryServiceV1;
import com.sparta.bapzip.category.domain.exception.CategoryException;
import com.sparta.bapzip.category.presentation.dto.request.CategoryRequestDto;
import com.sparta.bapzip.category.presentation.dto.response.CategoryDetailResponse;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.shop.presentation.dto.response.ShopDetailForUserResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
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
                                            value = """
                                                        {
                                                           "success": true,
                                                           "code": 200,
                                                           "data": {
                                                             [
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
                                                             [
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
                                                                  },
                                                                  {
                                                                      "id": "80bfada2-d20d-441d-8cb8-5fe3ec96c9f3",
                                                                      "name": "피자",
                                                                      "content": "피자",
                                                                      "deleted": true
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
                                                         "serviceAreaId": "dd86cd69-aa1e-4d66-9fdb-4542d47ead7d",
                                                         "areaName": "광화문 지역",
                                                         "pointList": [
                                                           {
                                                             "x": 37.583597,
                                                             "y": 126.969919
                                                           },
                                                           {
                                                             "x": 37.583469,
                                                             "y": 126.981209
                                                           },
                                                           {
                                                             "x": 37.574448,
                                                             "y": 126.98107
                                                           },
                                                           {
                                                             "x": 37.573268,
                                                             "y": 126.969774
                                                           },
                                                           {
                                                             "x": 37.583597,
                                                             "y": 126.969919
                                                           }
                                                         ],
                                                         "isService": true
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
