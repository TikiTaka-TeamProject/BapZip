package com.sparta.bapzip.servicearea.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.global.exception.GlobalException;
import com.sparta.bapzip.servicearea.application.dto.AreaReturnDto;
import com.sparta.bapzip.servicearea.application.dto.AreaSaveDto;
import com.sparta.bapzip.servicearea.application.exception.ServiceAreaNotFoundException;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceAreaServiceV1 {

    private final ServiceAreaRepository serviceAreaRepository;

    public ServiceAreaEntity getServiceAreaById(UUID serviceAreaId) {
        return serviceAreaRepository.getServiceAreaById(serviceAreaId)
                .orElseThrow(() -> new ServiceAreaNotFoundException(ErrorCode.SERVICE_AREA_NOT_FOUND));
    }

    /**
     * ServiceAreaEntity를 생성 및 저장합니다.
     * @param requestDto {@link AreaSaveDto}
     * @return {@link AreaReturnDto}
     */
    public AreaReturnDto createServiceArea(AreaSaveDto requestDto){
        return AreaReturnDto.from(serviceAreaRepository.save(ServiceAreaEntity.create(requestDto)));
    }

    /**
     * 특정 Point가 주어진 Polygon을 가진 엔티티 내에 포함되는지 여부를 확인합니다.
     * @param serviceAreaId 확인할 Polygon 엔티티의 ID
     * @param longitude Point의 경도 (X 좌표)
     * @param latitude Point의 위도 (Y 좌표)
     * @return Polygon이 Point를 포함하면 TRUE, 아니면 FALSE
     */
    public Boolean isExistenceArea(UUID serviceAreaId, Double longitude, Double latitude){
        return serviceAreaRepository.isExistenceArea(serviceAreaId, longitude, latitude);
    }

    public ServiceAreaEntity getServiceAreaByPoint(Double longitude, Double latitude) {
        return serviceAreaRepository.findByPoint(longitude, latitude)
                .orElseThrow(() -> new ServiceAreaNotFoundException(ErrorCode.SERVICE_AREA_NOT_FOUND));
    }

    /**
     * 서비스 지역 이름으로 Polygon 조회 (부분 일치 가능)
     *
     * @param name 서비스 지역 이름 일부
     * @return Polygon 해당 서비스 영역
     * @throws GlobalException 존재하지 않으면 예외 발생
     */
    public Polygon getServiceAreaPolygonByName(String name) {
        // 이름으로 LIKE 검색
        List<ServiceAreaEntity> serviceAreas = serviceAreaRepository.findByNameLike(name);

        if (serviceAreas.isEmpty()) {
            throw new ServiceAreaNotFoundException(ErrorCode.SERVICE_AREA_NOT_FOUND);
        }

        // 첫 번째 결과를 사용 (원하면 우선순위 로직 추가 가능)
        Polygon polygon = serviceAreas.get(0).getAreaPolygon();

        // SRID 설정 (없으면 4326으로)
        if (polygon.getSRID() == 0) {
            polygon.setSRID(4326);
        }

        return polygon;
    }
}
