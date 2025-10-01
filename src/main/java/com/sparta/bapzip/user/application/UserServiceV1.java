package com.sparta.bapzip.user.application;

import com.sparta.bapzip.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {

    private final UserRepository userRepository;

    public void test(){
        userRepository.findById(1L);
    }
}
