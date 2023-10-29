package com.example.springchallenge2week.common.utils.cookie;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    // Private constructor to prevent instantiation
    private CookieUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void addCookie(HttpServletResponse response, String name, String value, String seconds) {

        Long maxAge = Long.parseLong(seconds);
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void setRefreshTokenCookie(HttpServletResponse response, String name, String value, String seconds) {

        Long maxAge = Long.parseLong(seconds);
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(maxAge)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteRefreshTokenCookie(HttpServletResponse response, String refresh_token) {

        ResponseCookie cookie = ResponseCookie.from(refresh_token, "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0L)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}
