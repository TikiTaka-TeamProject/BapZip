package com.sparta.bapzip.user.domain.repository;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByEmail(String email);

    UserEntity save(UserEntity user);

    Page<UserEntity> findAll(Pageable pageable);

    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);
}
