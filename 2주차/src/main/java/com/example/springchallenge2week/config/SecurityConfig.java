package com.example.springchallenge2week.config;

import com.example.springchallenge2week.common.security.jwt.JwtAccessDeniedHandler;
import com.example.springchallenge2week.common.security.jwt.JwtAuthenticationEntryPoint;
import com.example.springchallenge2week.common.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity                                  // Spring Security 설정할 클래스라고 정의
@EnableGlobalMethodSecurity(prePostEnabled = true)  //  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')") 사용하기 위해 추가
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint; // 401 에러 처리
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;        // 403 에러 처리
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                // form login 전략 x
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                // jwt 설정
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)     // 401 에러 처리
                .accessDeniedHandler(jwtAccessDeniedHandler)            // 403 에러 처리
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/user/signup", "/api/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/logout", "/api/user/reissuance").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/user/info").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/api/coupon").hasAnyAuthority("ADMIN")

    // 401, 403 exception handling 처리 -> authenticationEntryPoint, jwtAccessDeniedHandler로 대체
//                .and()
//                        .exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.getWriter().write("{\"code\": \"401\",\"message\": \"로그인이 안되었습니다.\"}");
//                        });
        ;
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();  // iframe 허용안함
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/robot.txt",
                "/favicon.ico"
        );
    }

    // CORS 설정 -> @CrossOrigin 어노테이션으로 대체 가능
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("corsConfigurationSource");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");           // 모든 HTTP Method 허용
        configuration.addAllowedOriginPattern("*");    // 모든 IP 주소 허용 -> 나중에 프론트앤드 IP 주소만 허용되게 변경
        configuration.setAllowCredentials(true);       // 클라이언트에게 쿠키 허용
        configuration.setMaxAge(3600L);                // 쿠키 유효시간 1시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위의 설정 적용
        return source;
    }

}
