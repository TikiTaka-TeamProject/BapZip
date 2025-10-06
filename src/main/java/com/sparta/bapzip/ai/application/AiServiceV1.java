package com.sparta.bapzip.ai.application;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
     * @param userId 질문한유저 식별자 {@link UserEntity}
     * @param menuId 해당메뉴 식별자 {@link MenuEntity}
     * @return response 질문에 대한 답변
     */
    public String getResponse(String prompt, Long userId, UUID menuId){
        String response = aiCallable.getResponse(prompt);
        UserEntity userEntity = userRepository.findById(userId).get();
        AiEntity aiEntity = AiEntity.builder()
                .prompt(prompt)
                .response(response)
                .user(userEntity)
                .menuId(menuId)
                .build();
        aiLogRepository.save(aiEntity);
        return response;
    }

}
