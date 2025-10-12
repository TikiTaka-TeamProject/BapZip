package com.sparta.bapzip.ai.application.dto;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class AiLogResponseDto {

    private final  UUID id;
    private final Long userId;
    private final UUID menuId;
    private final String prompt;
    private final String response;
    private final LocalDateTime createdAt;

    public static AiLogResponseDto from(AiEntity aiEntity){
        return new AiLogResponseDto(
                aiEntity.getId(),
                aiEntity.getUser().getId(),
                aiEntity.getMenuId(),
                aiEntity.getPrompt(),
                aiEntity.getResponse(),
                aiEntity.getCreatedAt()
        );
    }



}
