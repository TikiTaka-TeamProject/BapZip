package com.sparta.bapzip.ai.application;

import com.sparta.bapzip.ai.application.dto.AiLogResponseDto;
import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.ai.application.exception.AiLogNotFoundException;
import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiServiceV1 {

    private final AiLogRepository aiLogRepository;
    private final AiCallable aiCallable;

    // TODO: 타도메인간 통신 Service 개발 후 수정 필요
    private final UserRepository userRepository;

    /**
     * <p>질문에 대한 Ai 답변 생성</p>
     * @param prompt 질문내용
     * @param user 질문한유저 {@link UserEntity}
     * @param menuId 해당메뉴 식별자 {@link MenuEntity}
     * @return response 질문에 대한 답변
     */
    public String getResponse(String prompt, UserEntity user, UUID menuId){
        String response = aiCallable.getResponse(prompt);
        AiEntity aiEntity = AiEntity.create(prompt, response, user, menuId);
        aiLogRepository.save(aiEntity);
        return response;
    }

    /**
     * <p>Ai log 단건 조회</p>
     * @param aiLogId 식별자
     * @return AiLogResponseDto {@link AiLogResponseDto}
     */
    public AiLogResponseDto getAiLog(UUID aiLogId){
        AiEntity aiEntity = aiLogRepository.findById(aiLogId).orElseThrow(
                ()-> new AiLogNotFoundException(ErrorCode.AI_LOG_NOT_FOUND)
        );
        return AiLogResponseDto.from(aiEntity);
    }

    // TODO: security 연동이후 UserEntity 객체를 바로 받아오도록 수정필요
    /**
     * <p>Ai log 다건 조회</p>
     * @param user UserEntity
     * @param pageable Pageable
     * @return Page<AiLogResponseDto> Ai-log 리스트
     */
    @Transactional(readOnly = true)
    public Page<AiLogResponseDto> getAiLogs(UserEntity user, Pageable pageable) {
        Page<AiEntity> aiLogList = aiLogRepository.findAllByUser(user,pageable);
        return aiLogList.map(AiLogResponseDto::from);
    }


}
