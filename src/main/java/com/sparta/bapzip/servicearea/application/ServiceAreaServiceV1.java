package com.sparta.bapzip.servicearea.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.application.dto.AreaReturnDto;
import com.sparta.bapzip.servicearea.application.dto.AreaSaveDto;
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

    public AreaReturnDto createServiceArea(AreaSaveDto requestDto){
        return AreaReturnDto.from(serviceAreaRepository.save(ServiceAreaEntity.create(requestDto)));
    }

    public Boolean isExistenceArea(UUID serviceAreaId, Double x, Double y){
        return serviceAreaRepository.isExistenceArea(serviceAreaId,x,y);
    }

}
