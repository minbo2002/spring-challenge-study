package com.example.springchallenge2week.domain.user.service;

import com.example.springchallenge2week.domain.user.dto.request.SignUpDto;
import com.example.springchallenge2week.domain.user.dto.response.UserInfoDto;

public interface UserService {

    void registerUser(SignUpDto signUpDto);

    UserInfoDto getUser(Long userId);
}
