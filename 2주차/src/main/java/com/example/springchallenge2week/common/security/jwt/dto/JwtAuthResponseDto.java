package com.example.springchallenge2week.common.security.jwt.dto;

import com.example.springchallenge2week.domain.user.entity.Role;
import com.example.springchallenge2week.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class JwtAuthResponseDto {

    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String email;
    private Set<String> roles;

    public JwtAuthResponseDto(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = user.getId();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
