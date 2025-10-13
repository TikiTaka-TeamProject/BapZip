package com.sparta.bapzip.ai.presentation.controller;

import com.sparta.bapzip.ai.application.AiServiceV1;
import com.sparta.bapzip.ai.application.dto.AiLogResponseDto;
import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/ai-logs")
public class AiControllerV1 {

    private final AiServiceV1 aiServiceV1;


    @GetMapping("/{aiLogId}")
    public ResponseEntity<ApiResponse<AiLogResponseDto>> getAiLog(@PathVariable UUID aiLogId){
        AiLogResponseDto aiLogResponseDto = aiServiceV1.getAiLog(aiLogId);
        return ApiResponse.ok(aiLogResponseDto);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<AiLogResponseDto>>> getAiLogs(
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ApiResponse.ok(aiServiceV1.getAiLogs(userDetails.getUser(), pageable));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> getResponse(
            @RequestParam(value = "prompt") String prompt,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
      return ApiResponse.ok(aiServiceV1.getResponse(prompt,userDetails.getUser(),UUID.randomUUID()));
    }
}
