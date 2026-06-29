package com.myproj.selfmade.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository {

    // refresh token 앞에 항상 붙일 값 (refresh token의 줄임말)
    private static final String KEY_PREFIX="RT:";

    private final StringRedisTemplate redisTemplate;

    // 프리픽스 + 이메일(키), 발급된 refresh token(값), 만기일(밀리초)로 db에 저장
    public void save(String email, String refreshToken, long expiration){
        redisTemplate.opsForValue()
                .set(KEY_PREFIX+email, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    // 이메일로 refresh token 저장
    public String find(String email){
        return redisTemplate.opsForValue().get(KEY_PREFIX+email);
    }

    // 이메일로 삭제
    public void delete(String email){
        redisTemplate.delete(KEY_PREFIX+email);
    }

}
