package com.example.springchallenge2week.common.dto;

import com.example.springchallenge2week.common.exception.ResponseCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ResultDto<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime timestamp;
    private final Integer code; // 200, 400, 500
    private final String message;
    private final String detail;
    private final T data;


    public static <T> ResultDto<T> success() {
        return ResultDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(200)
                .message(ResponseCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> ResultDto<T> success(T data) {
        return ResultDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(200)
                .message(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }


    public static <T> ResultDto<T> fail(ResponseCode responseCode, String message, T data) {
        return ResultDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .detail(message)
                .data(data)
                .build();
    }
}
