package com.example.ssru.security.jwt;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    // 실제 필터링 로직
    // 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        if ("/login".equals(requestURI) || "/sign-in".equals(requestURI) || requestURI.startsWith("/check") || requestURI.startsWith("/emails") || requestURI.startsWith("/phone")) {
            chain.doFilter(request, response);
        } else{
            String token = resolveToken((HttpServletRequest) request);

            // 토큰 유효성 검사
            if(token != null && tokenProvider.validateToken(token)){
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }
        }
    }

    // Request Header 에서 토큰 정보를 꺼내오기 위한 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }
}