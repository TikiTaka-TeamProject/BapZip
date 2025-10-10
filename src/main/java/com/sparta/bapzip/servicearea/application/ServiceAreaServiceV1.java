package com.sparta.bapzip.servicearea.application;

import com.sparta.bapzip.category.domain.entity.CategoryEntity;
import com.sparta.bapzip.category.infrastructure.repository.CategoryJpaRepository;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceAreaServiceV1 {

    private final ServiceAreaRepository serviceAreaRepository;

    public ServiceAreaEntity getServiceAreaById(UUID serviceAreaId) {
        return serviceAreaRepository.getServiceAreaById(serviceAreaId)
                .orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_AREA_NOT_FOUND));
    }
}
