package com.sparta.bapzip.user.presentation.controller;

import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.presentation.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.presentation.dto.response.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto requestDto) {
        return userServiceV1.signup(requestDto);
    }
}
