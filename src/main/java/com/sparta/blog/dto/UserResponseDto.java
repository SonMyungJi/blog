package com.sparta.blog.dto;

import com.sparta.blog.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private String username;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
    }
}
