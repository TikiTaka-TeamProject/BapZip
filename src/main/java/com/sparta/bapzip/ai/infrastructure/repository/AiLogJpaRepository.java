package com.sparta.bapzip.ai.infrastructure.repository;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiLogJpaRepository extends JpaRepository<AiEntity, UUID> {

}
