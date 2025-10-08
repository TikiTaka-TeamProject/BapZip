package com.sparta.bapzip.user.domain.repository;

import com.sparta.bapzip.user.domain.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByEmail(String email);

    UserEntity save(UserEntity user);
}
