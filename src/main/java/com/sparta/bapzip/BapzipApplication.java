package com.sparta.bapzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BapzipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BapzipApplication.class, args);
    }

}
