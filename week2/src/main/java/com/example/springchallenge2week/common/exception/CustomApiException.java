package com.example.springchallenge2week.common.exception;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

    private final ResponseCode responseCode;

    public CustomApiException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public CustomApiException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }
}
