package com.sparta.bapzip.ai.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GenerateTextFromTextInputTest {

    @Autowired
    private GenerateTextFromTextInput service;


    @Test
    void getCompletion() {
        String answer = service.getResponse("서울 맛집을 추천해줘");
        System.out.println(answer);
    }
}