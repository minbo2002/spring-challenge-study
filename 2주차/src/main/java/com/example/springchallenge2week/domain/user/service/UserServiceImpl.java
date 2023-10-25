package com.example.springchallenge2week.domain.user.service;

import com.example.springchallenge2week.common.exception.CustomApiException;
import com.example.springchallenge2week.common.exception.ResponseCode;
import com.example.springchallenge2week.domain.user.dto.response.UserResponse;
import com.example.springchallenge2week.domain.user.entity.User;
import com.example.springchallenge2week.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(ResponseCode.NO_TARGET));

        return UserResponse.toDto(user);
    }
}
