package com.sparta.bapzip.servicearea.domain.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;

import java.util.Optional;
import java.util.UUID;

public interface ServiceAreaRepository {
    Optional<ServiceAreaEntity> getServiceAreaById(UUID serviceAreaId);

    ServiceAreaEntity save(ServiceAreaEntity serviceAreaEntity);

    Boolean isExistenceArea(UUID id, Double x, Double y);

    Optional<ServiceAreaEntity> findByPoint(Double longitude, Double latitude);
}
