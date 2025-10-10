package com.sparta.bapzip.user.presentation.controller;

import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserDeleteRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.application.dto.response.SignupResponseDto;
import com.sparta.bapzip.user.application.dto.response.UserDeleteResponseDto;
import com.sparta.bapzip.user.application.dto.response.UserResponseDto;
import com.sparta.bapzip.user.application.dto.response.UserUpdateResponseDto;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import jakarta.validation.Valid;
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
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userServiceV1.signup(requestDto);
    }

    @GetMapping
    public Page<UserResponseDto> getUserList(@RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                             @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                             @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        return userServiceV1.getUserList(page - 1, size, sortBy, isAsc);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userServiceV1.getUser(userId, userDetails.getUser());
    }

    @PatchMapping
    public UserUpdateResponseDto updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userServiceV1.updateUser(userUpdateRequestDto, userDetails.getUser());
    }

    @DeleteMapping
    public UserDeleteResponseDto deleteUser(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userServiceV1.deleteUser(userDeleteRequestDto, userDetails.getUser());
    }
}
