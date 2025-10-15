package com.sparta.bapzip.servicearea.infrastructure.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
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

    @Override
    public ServiceAreaEntity save(ServiceAreaEntity serviceAreaEntity) {
        return serviceAreaJpaRepository.save(serviceAreaEntity);
    }

    @Override
    public Boolean isExistenceArea(UUID id, Double x, Double y) {
        return serviceAreaJpaRepository.isExistenceArea(id, x, y);
    }

    @Override
    public Optional<ServiceAreaEntity> findByPoint(Double longitude, Double latitude) {
        return serviceAreaJpaRepository.findByPoint(longitude, latitude);
    }

    @Override
    public Optional<ServiceAreaEntity> findByName(String name) {
        return serviceAreaJpaRepository.findByName(name);
    }

    @Override
    public List<ServiceAreaEntity> findByNameLike(String name) {
        return serviceAreaJpaRepository.findByNameLike(name);
    }

}
