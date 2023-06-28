package com.sparta.blog.controller;

import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.dto.UserResponseDto;
import com.sparta.blog.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/user/signup")
    public UserResponseDto signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return new UserResponseDto(true);
    }
}