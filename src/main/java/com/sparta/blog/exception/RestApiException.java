package com.sparta.blog.exception;

import lombok.Getter;

@Getter
public class RestApiException {
    private String errorMessage;
    private int statusCode;

    public RestApiException(String errorMessage, int statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}
