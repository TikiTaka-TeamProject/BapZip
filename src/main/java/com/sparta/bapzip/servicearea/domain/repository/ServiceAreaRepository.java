package com.sparta.bapzip.servicearea.domain.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface ServiceAreaRepository {
    // TODO: 용은 -> Shop 테스트작업을 위해 생성. 필요없을 시 제거
    Optional<ServiceAreaEntity> getServiceAreaById(UUID serviceAreaId);
}
