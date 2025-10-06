package com.sparta.bapzip.ai.application;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AiServiceV1Test {

    @Autowired
    private AiServiceV1 service;

    @Autowired
    private UserRepository userRepository;


    @Test
    void getResponse() {

        UserEntity user = userRepository.findById(1L).get();
        String prompt = "만두메뉴 이름 추천해줘";
        UUID menuId = UUID.randomUUID();
        String response = service.getResponse(prompt,1L,menuId);
        System.out.println(response);
    }
}