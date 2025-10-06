package com.sparta.bapzip.ai.presentation.controller;

import com.sparta.bapzip.ai.application.AiServiceV1;
import com.sparta.bapzip.ai.application.dto.AiLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
