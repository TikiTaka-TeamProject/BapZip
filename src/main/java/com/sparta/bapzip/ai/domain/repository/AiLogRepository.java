package com.sparta.bapzip.ai.domain.repository;


import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AiLogRepository {

    void save(AiEntity entity);

    Optional<AiEntity> findById(UUID aiLogId);

    Page<AiEntity> findAllByUser(UserEntity user, Pageable pageable);
}
