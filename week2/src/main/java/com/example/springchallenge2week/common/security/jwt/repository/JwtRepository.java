package com.example.springchallenge2week.common.security.jwt.repository;

import com.example.springchallenge2week.common.security.jwt.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JwtRepository extends JpaRepository<Token, Long> {

    void deleteByRefreshToken(String refreshToken);

    @Query("SELECT t FROM Token t WHERE t.accessToken = :accessToken AND t.refreshToken = :refreshToken AND t.expiredAt > :now")
    Optional<Token> findByAccessTokenAndRefreshTokenAndExpiredAtGreaterThan(@Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken, @Param("now") LocalDateTime now);

    Optional<Token> findByRefreshTokenAndExpiredAtGreaterThan(String refreshToken, LocalDateTime now);
}
