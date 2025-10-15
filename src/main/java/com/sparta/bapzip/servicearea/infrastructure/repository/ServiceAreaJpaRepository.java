package com.sparta.bapzip.servicearea.infrastructure.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceAreaJpaRepository extends JpaRepository<ServiceAreaEntity, UUID> {

    @Query(value = "SELECT ST_Contains(y.area, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) " +
            "FROM ServiceAreaEntity y " +
            "WHERE y.id = :entityId"
            )
    Boolean isExistenceArea(
            @Param("entityId") UUID entityId,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude
    );

    // 특정 Point가 속한 ServiceArea 조회
    @Query(value = "SELECT * FROM p_service_areas s " +
            "WHERE ST_Contains(s.area, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326))",
            nativeQuery = true)
    Optional<ServiceAreaEntity> findByPoint(
            @Param("longitude") double longitude, //x
            @Param("latitude") double latitude //y
    );

    Optional<ServiceAreaEntity> findByName(String name);

    @Query("""
    SELECT sa
    FROM ServiceAreaEntity sa
    WHERE LOWER(sa.name) LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    List<ServiceAreaEntity> findByNameLike(@Param("name") String name);
}
