package com.myproj.selfmade.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @CorsConfig 가 하는 일
 * 백엔드가 응답 헤더에 "이 출처는 허용한다" 는 표시를 붙여주는 것.
 * 브라우저가 그걸 보고 통과시켜주는 흐름
 *
 * CORS란?
 * Cross-Origin Resource Sharing — 직역하면 "다른 출처 간 자원 공유"야.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new  CorsConfiguration();

        // 허용할 출처 (React 개발 서버)
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        // 허용할 HTTP 메소드
        config.setAllowedMethods(List.of("GET",  "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 허용할 헤더
        config.setAllowedHeaders(List.of("*"));

        // 인증 정보(쿠키, Authorization 헤더) 포함 허용
        config.setAllowCredentials(true);

        // 프론트가 읽을 수 있는 응답 헤더
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로애 위 설정 적용
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
