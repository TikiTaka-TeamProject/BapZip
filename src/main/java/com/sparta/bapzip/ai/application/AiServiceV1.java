package com.sparta.bapzip.ai.application;

import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiServiceV1 {

    private final AiLogRepository aiLogRepository;

}
