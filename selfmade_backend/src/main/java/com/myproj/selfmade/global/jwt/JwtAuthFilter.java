package com.myproj.selfmade.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    // 클라이언트가 서버에 HTTP 요청을 보낼 때마다 작동
    // controller에 도달하기 전에 무조건 가치는 관문
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Http 요청에서 토큰값만 꺼내기
        String token = resolveToken(request);

        // 유효성 검사 통과했다면?
        if(token != null && jwtProvider.isValid(token)){

            // 이메일과 role을 security context에 등록해주기
            String email = jwtProvider.getEmail(token);
            String role = jwtProvider.getRole(token);
            List<GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_"+role));

            // "이 사람 인증됬어" 라는 증명서
            // 매개변수 -> 주체, 비밀번호(credential, 토큰으로 인증했으니까 필요없어서 null), 권한 목록
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            // 저장해놓고 나중에 controller에서 @AuthenticationPrincipal로 꺼내옴
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 이거 호출 안해주면 요청이 controller까지 못감
        // 요청이 여러개의 필터가 체인 형태로 연결되어 있고 모든 필터를 거쳐야 함
        filterChain.doFilter(request, response);

    }

    // 만약 토큰의 맨 앞 값이 "Bearer "로 시작한다면 정싱적인 토큰이니까
    // 그 토큰의 뒤의 진짜 값들만 갖고오는 유틸리티 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if(bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}