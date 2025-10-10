package com.sparta.bapzip.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteRequestDto {

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$")
    private String password;
}
