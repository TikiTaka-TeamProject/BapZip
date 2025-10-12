package com.sparta.bapzip.servicearea.infrastructure.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ServiceAreaJpaRepository extends JpaRepository<ServiceAreaEntity, UUID> {


    /**
     * 특정 Point가 주어진 Polygon을 가진 엔티티 내에 포함되는지 여부를 확인합니다.
     * ST_Contains 함수 자체의 BOOLEAN 결과를 직접 반환합니다.
     * @param entityId 확인할 Polygon 엔티티의 ID
     * @param longitude Point의 경도 (X 좌표)
     * @param latitude Point의 위도 (Y 좌표)
     * @return Polygon이 Point를 포함하면 TRUE, 아니면 FALSE
     */
    @Query(value = "SELECT ST_Contains(y.area, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) " +
            "FROM ServiceAreaEntity y " +
            "WHERE y.id = :entityId"
            )
    Boolean isExistenceArea(
            @Param("entityId") UUID entityId,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude
    );
}
