package com.example.springchallenge2week.domain.user.service;

import com.example.springchallenge2week.domain.user.dto.response.UserResponse;

public interface UserService {

    UserResponse getUser(Long userId);
}
