package com.sparta.blog.dto;

import com.sparta.blog.entity.UserRoleEnum;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$",
    message = "최소 4자 이상, 10자 이하로 이루어진 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[_!#$%&'*+/=?`{|}~^.-])[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{8,15}$",
    message = "최소 8자 이상, 15자 이하로 이루어진 알파벳, 숫자, 특수문자로 구성되어야 합니다.")
    private String password;

    private UserRoleEnum role;
}
