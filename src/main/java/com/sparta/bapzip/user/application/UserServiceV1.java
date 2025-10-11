package com.sparta.bapzip.user.application;

import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.user.application.dto.request.UserDeleteRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserRoleChangeRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.application.dto.response.*;
import com.sparta.bapzip.user.application.excpetion.DuplicateUserException;
import com.sparta.bapzip.user.application.excpetion.PasswordNotMatchException;
import com.sparta.bapzip.user.application.excpetion.UnauthorizedUserException;
import com.sparta.bapzip.user.application.excpetion.UserNotFoundException;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        // 권한 확인
        UserRoleEnum role = requestDto.getRole();
        if (role.equals(UserRoleEnum.MANAGER) || role.equals(UserRoleEnum.MASTER)) {
            throw new UnauthorizedUserException(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
        }

        // 이메일 중복 확인
        String email = requestDto.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateUserException(ErrorCode.DUPLICATE_USER_EXCEPTION);
        }

        UserEntity user = UserEntity.create(requestDto, passwordEncoder.encode(requestDto.getPassword()));
        UserEntity saveUser = userRepository.save(user);
        return SignupResponseDto.of(saveUser);
    }

    public Page<UserResponseDto> getUserList(int page, int size, String sortBy, boolean isAsc) {
        if(size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserEntity> userList = userRepository.findAll(pageable);
        return userList.map(UserResponseDto::of);
    }

    public UserResponseDto getUser(Long userId, UserEntity user) {
        // 요청 유저 id와 토큰 유저의 id가 같을 경우 유저 정보 반환
        if (userId.equals(user.getId())) {
            return UserResponseDto.of(user);
        }

        // 요청 유저 id와 토큰 유저의 id가 다를 경우
        // 토큰 유저의 role이 MASTER와 MANAGER인지 판별 후 유저 정보 반환
        UserRoleEnum role = user.getRole();
        if (role.equals(UserRoleEnum.MANAGER) || role.equals(UserRoleEnum.MASTER)) {
            return UserResponseDto.of(findUser(userId));
        }

        // 토큰 유저의 role이 MASTER와 MANAGER가 아닐 경우 exception 반환
        throw new UnauthorizedUserException(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
    }

    public UserUpdateResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto, UserEntity user) {
        // 비밀번호가 일치 하는지 확인
        matchPassword(userUpdateRequestDto.getPassword(), user.getPassword());

        // 비밀번호가 일치하면 유저 이름과 변경할 패스워드 업데이트
        user.update(userUpdateRequestDto, passwordEncoder);
        user.markUpdated(user.getId());
        UserEntity saveUser = userRepository.save(user);

        return UserUpdateResponseDto.of(saveUser);
    }

    public UserDeleteResponseDto deleteUser(UserDeleteRequestDto userDeleteRequestDto, UserEntity user) {
        matchPassword(userDeleteRequestDto.getPassword(), user.getPassword());
        user.markDeleted(user.getId());
        UserEntity saveUser = userRepository.save(user);
        return UserDeleteResponseDto.of(saveUser);
    }

    public UserRoleChangeResponseDto changeUserRole(Long userId, UserRoleChangeRequestDto userRoleChangeRequestDto, UserEntity user) {
        UserRoleEnum role = userRoleChangeRequestDto.getRole();
        // MASTER도 role을 MASTER로 변경 불가
        if (role.equals(UserRoleEnum.MASTER)) {
            throw new UnauthorizedUserException(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
        }

        UserEntity targetUser = findUser(userId);
        targetUser.changeRole(role);
        targetUser.markUpdated(user.getId());

        return UserRoleChangeResponseDto.of(targetUser);
    }

    private UserEntity findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION)
        );
    }

    private void matchPassword(String rowPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rowPassword, encodedPassword)) {
            throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH_EXCEPTION);
        }
    }
}
