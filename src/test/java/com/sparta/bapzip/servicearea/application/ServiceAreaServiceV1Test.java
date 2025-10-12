package com.sparta.bapzip.servicearea.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceAreaServiceV1Test {

    @Autowired
    ServiceAreaServiceV1 serviceAreaServiceV1;

    @Test
    void isExistenceArea() {

        UUID id = UUID.fromString("c37fcc08-f254-4b8d-8257-5c8cbb6a9641");
        Double x = 37.574825;
        Double y = 126.974971;
        Boolean result = serviceAreaServiceV1.isExistenceArea(id,x,y);

        System.out.println(result);
    }
}