package com.sparta.bapzip.ai.presentation.controller;

import com.sparta.bapzip.ai.application.AiServiceV1;
import com.sparta.bapzip.ai.application.dto.AiLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/ai-logs")
public class AiControllerV1 {

    private final AiServiceV1 aiServiceV1;


    @GetMapping("/{aiLogId}")
    public ResponseEntity<AiLogResponseDto> getAiLog(@PathVariable UUID aiLogId){
        AiLogResponseDto aiLogResponseDto = aiServiceV1.getAiLog(aiLogId);
        return ResponseEntity.ok(aiLogResponseDto);
    }

    // TODO: security 연동이후 userId -> userDetails에서 user 객체를 바로 보낼수있도록 수정필요
    @GetMapping()
    public Page<AiLogResponseDto> getAiLogs(Pageable pageable, @RequestParam("userId") Long userId){
        return aiServiceV1.getAiLogs(userId, pageable);
    }
}
