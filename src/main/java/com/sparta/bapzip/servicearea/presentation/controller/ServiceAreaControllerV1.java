package com.sparta.bapzip.servicearea.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.servicearea.application.ServiceAreaServiceV1;
import com.sparta.bapzip.servicearea.application.dto.AreaSaveDto;
import com.sparta.bapzip.servicearea.application.dto.request.AreaSaveRequest;
import com.sparta.bapzip.servicearea.presentation.dto.response.AreaSaveResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/service-areas")
@RequiredArgsConstructor
@Tag(name = "서비스 지역", description = "서비스 지역 api")
public class ServiceAreaControllerV1 {

    private final ServiceAreaServiceV1 service;

    @Operation(summary = "서비스 지역 등록", description = "서비스 지역 등록 메서드 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "서비스 지역 등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = AreaSaveResponse.class),
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
    @PostMapping
    public ResponseEntity<ApiResponse<AreaSaveResponse>> saveServiceArea(
            @RequestBody AreaSaveRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ){
        return ApiResponse.created(
                AreaSaveResponse.from(
                        service.createServiceArea(AreaSaveDto.from(request))));
    }


}
