package com.myproj.selfmade.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// Bean으로 등록해라
// 근데 원래 이 @Component는 다른데 다 포함되어 있음 예를 들면 service라던가 controller라던가 다 포함되어 있고
// 우리는 그걸 명확히 표시해서 사용하기 위해 그렇게 바꿔서 사용하는 것이고
@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(String email, String role) {
        return generateToken(email, role, accessTokenExpiration);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, null, refreshTokenExpiration);
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateToken(String email, String role, long expiration) {
        Date now = new Date();
        var builder = Jwts.builder()
                        .subject(email)
                        .issuedAt(now)
                        .expiration(new Date(now.getTime() + expiration));

        if(role != null){
            builder.claim("role", role);
        }

        return builder.signWith(secretKey).compact();
    }

    public String getRole(String token){
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    // 2단계 — JwtAuthFilter에서 권한 등록
    // 여기서부터 시작하면 됨
}