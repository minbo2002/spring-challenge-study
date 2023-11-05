package com.example.springchallenge2week.common.security.jwt;

import com.example.springchallenge2week.common.security.dto.PrincipalDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;           // 설정 파일에서 jwt 토큰에 서명하는데 사용할 secret key를 가져온다.

    @Value("${jwt.accessTokenExpirationPeriod}")
    private int jwtExpirationInMs;      // 설정 파일에서 jwt 토큰의 유효기간을 가져온다.

    // generate token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);  //
        log.info("jwtExpirationInMs: {}", jwtExpirationInMs);
        log.info("expireDate: " + expireDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateToken(PrincipalDetails principalDetails) {
        String username = principalDetails.getUsername();
        Date currentDate = new Date();
//        Date expireDate = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));    // 7 days
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);    // 7 days
        log.info("jwtExpirationInMs: {}", jwtExpirationInMs);
        log.info("expireDate: " + expireDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // get username from the token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex){
            throw new RuntimeException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
//            throw new CustomApiException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
            throw new RuntimeException("JWT claims string is empty");
        }
    }

}
