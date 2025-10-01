package com.sparta.bapzip.user.application;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {

    private final UserRepository userRepository;


    public void test(){
        Optional<UserEntity> byId = userRepository.findById(1L);
        UserEntity userEntity = byId.get();

    }
}
