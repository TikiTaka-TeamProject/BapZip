package com.sparta.bapzip.ai.infrastructure.repository;

import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import com.sparta.bapzip.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AiLogRepositoryImpl implements AiLogRepository {

    private final UserJpaRepository userJpaRepository;


}
