package com.sparta.bapzip.ai.application;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiServiceV1 {

    private final AiLogRepository aiLogRepository;
    private final AiCallable aiCallable;


    /**
     * <p>질문에 대한 Ai 답변 생성</p>
     * @param prompt 질문내용
     * @param user 질문한사람 {@link UserEntity}
     * @param menu 해당메뉴 {@link MenuEntity}
     * @return response 질문에 대한 답변
     */
    public String getResponse(String prompt, UserEntity user, MenuEntity menu){
        String response = aiCallable.getResponse(prompt);
        AiEntity aiEntity = AiEntity.builder()
                .prompt(prompt)
                .response(response)
                .user(user)
                .menu(menu)
                .build();
        aiLogRepository.save(aiEntity);
        return response;
    }

}
