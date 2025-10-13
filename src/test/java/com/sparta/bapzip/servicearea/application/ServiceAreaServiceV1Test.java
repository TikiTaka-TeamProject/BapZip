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
                "[{x: 37.583597, y: 126.969919}," +
                        "{x: 37.583469, y: 126.981209}," +
                        "{x: 37.574448, y: 126.981070}," +
                        "{x: 37.573268, y: 126.969774}]";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        List<Point> area;
        try {
             area = objectMapper.readValue(areaJson, new TypeReference<List<Point>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 오류가 발생하여 area가 null입니다.", e);
        }
        AreaSaveRequest saveRequest = new AreaSaveRequest("name", area);
        AreaSaveDto areaSaveDto = AreaSaveDto.from(saveRequest);
        ServiceAreaEntity createEntity = ServiceAreaEntity.create(areaSaveDto);
        repository.save(createEntity);
        this.savedEntityId = createEntity.getId();

    }

    @Test
    @DisplayName("해당포인트가 서비스지역내에 있을경우")
    void isExistenceArea() {

        //given
        Double x = 37.574825;
        Double y = 126.974971;

        //when
        Boolean result = serviceAreaServiceV1.isExistenceArea(savedEntityId, x, y);

        //then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("해당포인트가 서비스지역내에 없을경우")
    void isExistenceArea2() {

        //given
        Double x = 37.576854;
        Double y = 126.991034;

        //when
        Boolean result = serviceAreaServiceV1.isExistenceArea(savedEntityId, x, y);

        //then
        Assertions.assertFalse(result);
    }
}