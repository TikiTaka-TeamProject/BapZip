package com.sparta.bapzip;

import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class BapzipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BapzipApplication.class, args);
    }

    @Bean
    public AuditorAware<Long> auditorProvider() {
        // 인증 정보를 가져와 사용자의 ID 반환
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> ((UserDetailsImpl) authentication.getPrincipal()).getUser().getId());
    }

}
