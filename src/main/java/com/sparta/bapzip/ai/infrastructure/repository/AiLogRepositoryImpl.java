package com.sparta.bapzip.ai.infrastructure.repository;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.ai.domain.repository.AiLogRepository;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class AiLogRepositoryImpl implements AiLogRepository {

    private final AiLogJpaRepository aiLogJpaRepository;


    @Override
    public void save(AiEntity entity) {
        aiLogJpaRepository.save(entity);
    }

    @Override
    public Optional<AiEntity> findById(UUID aiLogId) {
        return aiLogJpaRepository.findById(aiLogId);
    }

    @Override
    public Page<AiEntity> findAllByUser(UserEntity user, Pageable pageable) {
        return aiLogJpaRepository.findAllByUser(user, pageable);
    }
}
