package com.sparta.bapzip.user.application.dto.request;

import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleChangeRequestDto {

    @NotNull
    private UserRoleEnum role;
}
