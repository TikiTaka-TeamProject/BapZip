package com.sparta.bapzip.ai.application.dto;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AiLogResponseDto {

    private UUID id;
    private Long userId;
    private UUID menuId;
    private String prompt;
    private String response;
    private LocalDateTime createdAt;

    public AiLogResponseDto(AiEntity aiEntity){
        this.id = aiEntity.getId();
        this.userId = aiEntity.getUser().getId();
        this.menuId = aiEntity.getMenuId();
        this.prompt = aiEntity.getPrompt();
        this.response = aiEntity.getResponse();
        this.createdAt = aiEntity.getCreatedAt();
    }



}
