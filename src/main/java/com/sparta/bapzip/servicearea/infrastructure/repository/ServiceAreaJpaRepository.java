package com.sparta.bapzip.servicearea.infrastructure.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAreaJpaRepository extends JpaRepository<ServiceAreaEntity, Long> {
}
