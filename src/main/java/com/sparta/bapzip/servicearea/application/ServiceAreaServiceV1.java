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

    /**
     * 특정 Point가 주어진 Polygon을 가진 엔티티 내에 포함되는지 여부를 확인합니다.
     * ST_Contains 함수 자체의 BOOLEAN 결과를 직접 반환합니다.
     * @param serviceAreaId 확인할 Polygon 엔티티의 ID
     * @param longitude Point의 경도 (X 좌표)
     * @param latitude Point의 위도 (Y 좌표)
     * @return Polygon이 Point를 포함하면 TRUE, 아니면 FALSE
     */
    public Boolean isExistenceArea(UUID serviceAreaId, Double longitude, Double latitude){
        return serviceAreaRepository.isExistenceArea(serviceAreaId, longitude, latitude);
    }

}
