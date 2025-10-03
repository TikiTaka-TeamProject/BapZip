package com.sparta.bapzip.ai.infrastructure.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.sparta.bapzip.ai.application.AiCallable;
import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import com.sparta.bapzip.menu.domain.entity.MenuEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService implements AiCallable {

    public static final String GEMINI_MODEL = "gemini-2.5-flash";
    public static final String CHARACTER_LIMIT = "\n 답변을 최대한 간결하게 50자 이하로";

    @Value("${GOOGLEAI_API_KEY}")
    private String apiKey;

    private final AiLogRepository repository;

    public String getResponse(String prompt){
        Client client = Client.builder().apiKey(apiKey).build();

        GenerateContentResponse response =
                client.models.generateContent(
                        GEMINI_MODEL,
                        prompt+CHARACTER_LIMIT,
                        null);


        return response.text();
    }
}
