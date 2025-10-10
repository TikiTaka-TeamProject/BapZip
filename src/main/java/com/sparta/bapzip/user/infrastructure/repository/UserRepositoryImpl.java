package com.sparta.bapzip.user.infrastructure.repository;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return userJpaRepository.findAll(pageable);
    }
}
