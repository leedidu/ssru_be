package com.example.ssru.security;

import com.example.ssru.security.jwt.JwtAccessDeniedHandler;
import com.example.ssru.security.jwt.JwtAuthenticationEntryPoint;
import com.example.ssru.security.jwt.JwtSecurityConfig;
import com.example.ssru.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable() // 토큰이기 때문에 csrf 비활성화
                .cors().disable()
                .formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                .and() // 세션 사용X -> STATELESS
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한 설정
                .antMatchers("/sign-in").permitAll() // 회원가입 api
                .antMatchers("/emails/***").permitAll()
                .antMatchers("/login").permitAll() // 로그인 api
                .antMatchers("/phone/***").permitAll() // 휴대폰 인증

                .and() // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }
}
