package com.example.springchallenge2week.domain.user.controller;

import com.example.springchallenge2week.domain.user.dto.response.UserResponse;
import com.example.springchallenge2week.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {

        UserResponse user = userService.getUser(userId);

        return ResponseEntity.ok(user);
    }
}
