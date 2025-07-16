package com.example.ssru.security.jwt;

import com.example.ssru.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private Key key;
    private final UserRepository userRepository;
    private final long accessTokenValidTime = 1000L * 60 * 60 * 24;
    private final long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 30;


    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            UserRepository userRepository) {
        this.secret = secret;
        this.userRepository = userRepository;
    }

    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String id, String roles, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(id); // claims 생성 및 payload 설정

        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .claim("auth", roles)
                .setExpiration(new Date(date.getTime() + tokenValid)) // 토큰 유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, key) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }

    // Access Token 생성.
    public String createAccessToken(String id, String roles){
        return this.createToken(id, roles, accessTokenValidTime);
    }
    // Refresh Token 생성.
    public String createRefreshToken(String id, String roles) {
        return this.createToken(id, roles, refreshTokenValidTime);
    }

    public JwtToken createJwtToken(Authentication authentication) {
        com.example.ssru.user.entity.User user = userRepository.findByLoginId(authentication.getName());

        String authorities = user.getAuth();
        String accessToken = createAccessToken(authentication.getName(), user.getAuth());
        String refreshToken = createRefreshToken(authentication.getName(), user.getAuth());

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .auth(authorities)
                .build();
    }

    // 토큰 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}