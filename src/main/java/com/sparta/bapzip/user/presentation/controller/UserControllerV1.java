package com.sparta.bapzip.user.presentation.controller;

import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.application.dto.response.UserResponseDto;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.response.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto requestDto) {
        return userServiceV1.signup(requestDto);
    }

    @GetMapping
    public Page<UserResponseDto> getUserList(@RequestParam("page") int page,
                                             @RequestParam("size") int size,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("isAsc") boolean isAsc,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userServiceV1.getUserList(page - 1, size, sortBy, isAsc, userDetails.getUser());
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userServiceV1.getUser(userId, userDetails.getUser());
    }
}
