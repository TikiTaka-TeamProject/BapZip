package com.sparta.bapzip.ai.domain.repository;


import com.sparta.bapzip.ai.domain.entity.AiEntity;

import java.util.Optional;
import java.util.UUID;

public interface AiLogRepository {

    void save(AiEntity entity);

    Optional<AiEntity> findById(UUID aiLogId);

}
