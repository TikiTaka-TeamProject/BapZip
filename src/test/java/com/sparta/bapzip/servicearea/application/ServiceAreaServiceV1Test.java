package com.sparta.bapzip.servicearea.application;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bapzip.servicearea.application.dto.AreaSaveDto;
import com.sparta.bapzip.servicearea.application.dto.request.AreaSaveRequest;
import com.sparta.bapzip.servicearea.domain.Point;
import com.sparta.bapzip.servicearea.domain.entity.ServiceAreaEntity;
import com.sparta.bapzip.servicearea.domain.repository.ServiceAreaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;


@SpringBootTest
class ServiceAreaServiceV1Test {


    @Autowired
    private ServiceAreaServiceV1 serviceAreaServiceV1;
    @Autowired
    private ServiceAreaRepository repository;

    private UUID savedEntityId;

    @BeforeEach
    void setUp(){
        String areaJson =
                "[{x: 126.969919, y: 37.583597}," +   // x=longitude, y=latitude
                        "{x: 126.981209, y: 37.583469}," +
                        "{x: 126.981070, y: 37.574448}," +
                        "{x: 126.969774, y: 37.573268}]";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        List<Point> area;
        try {
            area = objectMapper.readValue(areaJson, new TypeReference<List<Point>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 오류가 발생하여 area가 null입니다.", e);
        }
        AreaSaveRequest saveRequest = new AreaSaveRequest("광화문 지역", area);
        AreaSaveDto areaSaveDto = AreaSaveDto.from(saveRequest);
        ServiceAreaEntity createEntity = ServiceAreaEntity.create(areaSaveDto);
        repository.save(createEntity);
        this.savedEntityId = createEntity.getId();
    }

    @Test
    @DisplayName("해당 포인트가 서비스지역 내에 있을 경우")
    void isExistenceArea() {
        //given
        Double longitude = 126.974971; // x=longitude
        Double latitude = 37.574825;   // y=latitude

        //when
        Boolean result = serviceAreaServiceV1.isExistenceArea(savedEntityId, longitude, latitude);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("해당 포인트가 서비스지역 내에 없을 경우")
    void isExistenceArea2() {
        //given
        Double longitude = 126.991034; // x=longitude
        Double latitude = 37.576854;   // y=latitude

        //when
        Boolean result = serviceAreaServiceV1.isExistenceArea(savedEntityId, longitude, latitude);

        //then
        Assertions.assertFalse(result);
    }
}