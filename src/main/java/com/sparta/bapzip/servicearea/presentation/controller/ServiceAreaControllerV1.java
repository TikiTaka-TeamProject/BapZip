package com.sparta.bapzip.servicearea.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.servicearea.application.ServiceAreaServiceV1;
import com.sparta.bapzip.servicearea.application.dto.AreaSaveDto;
import com.sparta.bapzip.servicearea.application.dto.request.AreaSaveRequest;
import com.sparta.bapzip.servicearea.presentation.dto.response.AreaSaveResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
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
public class ServiceAreaControllerV1 {

    private final ServiceAreaServiceV1 service;


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
