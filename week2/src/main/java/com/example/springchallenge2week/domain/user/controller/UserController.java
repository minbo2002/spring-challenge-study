package com.example.springchallenge2week.domain.user.controller;

import com.example.springchallenge2week.common.props.JwtProps;
import com.example.springchallenge2week.common.security.dto.PrincipalDetails;
import com.example.springchallenge2week.common.security.jwt.dto.JwtAuthResponseDto;
import com.example.springchallenge2week.common.security.jwt.dto.JwtReissueRequestDto;
import com.example.springchallenge2week.common.security.jwt.entity.Token;
import com.example.springchallenge2week.common.security.jwt.service.JwtService;
import com.example.springchallenge2week.common.utils.cookie.CookieUtil;
import com.example.springchallenge2week.common.utils.encoder.PasswordEncoderUtil;
import com.example.springchallenge2week.domain.user.dto.request.LoginDto;
import com.example.springchallenge2week.domain.user.dto.request.SignUpDto;
import com.example.springchallenge2week.domain.user.dto.response.UserInfoDto;
import com.example.springchallenge2week.domain.user.entity.User;
import com.example.springchallenge2week.domain.user.repository.UserRepository;
import com.example.springchallenge2week.domain.user.service.UserService;
import com.example.springchallenge2week.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtUtil jwtUtil;
    private final JwtProps jwtProps;
    private final JwtService jwtService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        log.info("signUpDto: {}", signUpDto);

        userService.registerUser(signUpDto);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    // 로그인 (토큰발급)
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto,
                                              HttpServletResponse response) {

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with username or email : " + loginDto.getEmail()));

        if (!passwordEncoderUtil.checkPassword(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password is not correct");
        }

        // get accessToken from jwtTokenProvider
         String accessToken = jwtUtil.generateToken(new PrincipalDetails(user));  // Todo : user대신 new PrincipalDetails(user) 사용하면 에러 발생
//        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = UUID.randomUUID().toString();

        // insert accessToken, refreshToken to db
        jwtService.createJwt(accessToken, refreshToken, user);

        CookieUtil.setRefreshTokenCookie(response, "refresh_token", refreshToken, jwtProps.getRefreshTokenExpirationPeriod().toString());

//        return new ResponseEntity<>("User signed in successfully!", HttpStatus.OK);
        return ResponseEntity.ok(new JwtAuthResponseDto(accessToken, refreshToken, user));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        log.info("logout refreshToken: {}", refreshToken);
        jwtService.deleteByRefreshToken(refreshToken);
        CookieUtil.deleteRefreshTokenCookie(response, "refresh_token");
        return new ResponseEntity<>("User logged out successfully!", HttpStatus.OK);
    }

    // Reissue JWT Access Token & Refresh Token: JWT Access Token & Refresh Token 재발급
    @PostMapping("/reissuance")
    public ResponseEntity<?> refreshToken(
//            @RequestBody JwtReissueRequestDto requestDto
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        log.info("refreshToken refreshToken: {}", refreshToken);
        return jwtService.findByAccessTokenAndRefreshTokenAndExpireAtGreaterThan(refreshToken)
                .map(Token::getUser)
                .map(user -> {

                    // get accessToken from jwtTokenProvider
                    String accessToken = jwtUtil.generateToken(new PrincipalDetails(user));     // TODO : lazy 로딩 문제  (user대신 new PrincipalDetails(user) 사용하면 에러 발생)
//                    String accessToken = jwtUtil.generateToken(user);
                    String newRefreshToken = UUID.randomUUID().toString();

                    // insert accessToken, newRefreshToken to db and delete old refreshToken
                    jwtService.createJwt(accessToken, newRefreshToken, user);
                    jwtService.deleteByRefreshToken(refreshToken);

                    CookieUtil.setRefreshTokenCookie(response, "refresh_token", newRefreshToken, jwtProps.getRefreshTokenExpirationPeriod().toString());

                    return ResponseEntity.ok(new JwtAuthResponseDto(accessToken, newRefreshToken, user));
                })
                .orElseThrow(() -> new RuntimeException("User not found with username or email"));
    }

    // 유저정보 반환
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails: {}", principalDetails);

        UserInfoDto userInfoDto = userService.getUser(principalDetails.getUser().getId());
        return ResponseEntity.ok(userInfoDto);
    }
}
