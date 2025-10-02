package com.sparta.bapzip.user.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.user.application.excpetion.DuplicateUserException;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import com.sparta.bapzip.user.presentation.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.presentation.dto.response.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        // 이메일 중복 확인
        String email = requestDto.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateUserException(ErrorCode.DUPLICATE_USER_EXCEPTION);
        }

        UserEntity user = UserEntity.create(requestDto, passwordEncoder.encode(requestDto.getPassword()));
        UserEntity saveUser = userRepository.save(user);
        return SignupResponseDto.of(saveUser);
    }
}
