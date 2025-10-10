package com.sparta.bapzip.ai.infrastructure.repository;

import com.sparta.bapzip.ai.domain.entity.AiEntity;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiLogJpaRepository extends JpaRepository<AiEntity, UUID> {

    Page<AiEntity> findAllByUser(UserEntity user, Pageable pageable);
}
