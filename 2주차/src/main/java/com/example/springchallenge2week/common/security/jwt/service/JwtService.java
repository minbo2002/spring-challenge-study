package com.example.springchallenge2week.common.security.jwt.service;

import com.example.springchallenge2week.common.security.jwt.dto.JwtReissueRequestDto;
import com.example.springchallenge2week.common.security.jwt.entity.Token;
import com.example.springchallenge2week.common.security.jwt.repository.JwtRepository;
import com.example.springchallenge2week.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {

    private final JwtRepository jwtRepository;

    @Transactional
    public void createJwt(String accessToken, String refreshToken, User user) {
        log.info("JwtService insertJwt run...");
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
        jwtRepository.save(token);
    }

    @Transactional
    public void deleteByRefreshToken(String refreshToken) {
        log.info("JwtService deleteByRefreshToken run...");
        jwtRepository.deleteByRefreshToken(refreshToken);
    }

    public Optional<Token> findByDto(JwtReissueRequestDto requestDto) {

        String accessToken = requestDto.getAccessToken();
        String refreshToken = requestDto.getRefreshToken();
        LocalDateTime now = LocalDateTime.now();

        return jwtRepository.findByAccessTokenAndRefreshTokenAndExpiredAtGreaterThan(accessToken, refreshToken, now);
    }

    @Transactional
    public Optional<Token> findByAccessTokenAndRefreshTokenAndExpireAtGreaterThan(String refreshToken) {
        return jwtRepository.findByRefreshTokenAndExpiredAtGreaterThan(refreshToken, LocalDateTime.now());
    }
}
