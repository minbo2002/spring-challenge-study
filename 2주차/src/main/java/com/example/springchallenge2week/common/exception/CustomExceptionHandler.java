package com.example.springchallenge2week.common.exception;

import com.example.springchallenge2week.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // custom exception
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> handleCustomAPIException(
            CustomApiException exception,
            WebRequest webRequest) {

        log.error("handleCustomAPIException e", exception);

        return new ResponseEntity<>(ResultDto.fail(ResponseCode.INVALID_REQUEST, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
    }


    // global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
            Exception exception,
            WebRequest webRequest) {

        log.error("handleGlobalException e", exception);

        return new ResponseEntity<>(ResultDto.fail(ResponseCode.INTERNAL_SERVER_ERROR, exception.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //BindingResult Validation 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        log.error("handleMethodArgumentNotValid", exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(ResultDto.fail(ResponseCode.INVALID_REQUEST, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
