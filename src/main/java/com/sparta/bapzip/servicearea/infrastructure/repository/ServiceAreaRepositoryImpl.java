package com.sparta.bapzip.servicearea.infrastructure.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ServiceAreaRepositoryImpl implements ServiceAreaRepository {

    private final ServiceAreaJpaRepository serviceAreaJpaRepository;

    @Override
    public Optional<ServiceAreaEntity> getServiceAreaById(UUID serviceAreaId) {
        return serviceAreaJpaRepository.findById(serviceAreaId);
    }
}
