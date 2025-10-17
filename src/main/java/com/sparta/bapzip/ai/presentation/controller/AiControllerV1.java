package com.sparta.bapzip.ai.presentation.controller;

import com.sparta.bapzip.ai.application.AiServiceV1;
import com.sparta.bapzip.ai.application.dto.AiLogResponseDto;
import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.global.response.PageResponseDto;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Ai-log", description = "Ai-log api")
public class AiControllerV1 {

    private final AiServiceV1 aiServiceV1;

    @Operation(summary = "Ai-log 단건 조회", description = "Ai-log 단건조회 메서드 입니다.")
    @GetMapping("/{aiLogId}")
    public ResponseEntity<ApiResponse<AiLogResponseDto>> getAiLog(@PathVariable UUID aiLogId){
        AiLogResponseDto aiLogResponseDto = aiServiceV1.getAiLog(aiLogId);
        return ApiResponse.ok(aiLogResponseDto);
    }

    @Operation(summary = "Ai-log 다건 조회", description = "Ai-log 다건조회 메서드 입니다.")
    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponseDto<AiLogResponseDto>>> getAiLogs(
            Pageable pageable,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        Page<AiLogResponseDto> page = aiServiceV1.getAiLogs(userDetails.getUser(), pageable);
        return ApiResponse.ok(PageResponseDto.fromPage(page,pageable.getSort().toString(),isAsc));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> getResponse(
            @RequestParam(value = "prompt") String prompt,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
      return ApiResponse.ok(aiServiceV1.getResponse(prompt,userDetails.getUser(),UUID.randomUUID()));
    }
}
