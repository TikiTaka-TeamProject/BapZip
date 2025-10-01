package com.sparta.bapzip.user.infrastructure.repository;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
