package com.sparta.bapzip.servicearea.infrastructure.repository;

import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
