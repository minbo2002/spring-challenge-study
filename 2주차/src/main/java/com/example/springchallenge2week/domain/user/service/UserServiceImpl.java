package com.example.springchallenge2week.domain.user.service;

import com.example.springchallenge2week.common.exception.CustomApiException;
import com.example.springchallenge2week.common.exception.ResponseCode;
import com.example.springchallenge2week.common.utils.encoder.PasswordEncoderUtil;
import com.example.springchallenge2week.domain.user.dto.request.SignUpDto;
import com.example.springchallenge2week.domain.user.dto.response.UserInfoDto;
import com.example.springchallenge2week.domain.user.entity.Role;
import com.example.springchallenge2week.domain.user.entity.User;
import com.example.springchallenge2week.domain.user.repository.RoleRepository;
import com.example.springchallenge2week.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    // 회원가입
    @Transactional
    @Override
    public void registerUser(SignUpDto signUpDto) {
        log.info("userService registerUser run");

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new CustomApiException(ResponseCode.USERNAME_ALREADY_EXISTS);
        }
        // create user object
        // test role : admin
        Role role = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new CustomApiException(ResponseCode.USERNAME_ALREADY_EXISTS));
        log.info("role: {}", role);

        User user = User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .password(passwordEncoderUtil.encodePassword(signUpDto.getPassword()))
                .role(User.RoleType.ADMIN)
                .roles(Collections.singleton(role))
                .build();

        userRepository.save(user);
    }

    // 회원정보 조회
    @Override
    public UserInfoDto getUser(Long userId) {
        log.info("userService getUser run");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(ResponseCode.USER_NOT_FOUND));

        return UserInfoDto.from(user);
    }
}
