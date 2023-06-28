package com.sparta.blog.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private Boolean success;
    public UserResponseDto(Boolean success) { this.success = success; }
}
