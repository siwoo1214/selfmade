package com.myproj.selfmade.user.service;

import com.myproj.selfmade.global.exception.InvalidTokenException;
import com.myproj.selfmade.global.exception.UserNotFoundException;
import com.myproj.selfmade.global.jwt.JwtProvider;
import com.myproj.selfmade.global.jwt.RefreshTokenRepository;
import com.myproj.selfmade.user.dto.response.TokenResponseDto;
import com.myproj.selfmade.user.entity.User;
import com.myproj.selfmade.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public TokenResponseDto reissue(String refreshToken){

        // refresh token이 유효하지 않은 값이라면 예외 처리
        if(!jwtProvider.isValid(refreshToken)){
            throw new InvalidTokenException();
        }

        // 저장되닜는 refresh token에서 이메일 갖고오고
        String email = jwtProvider.getEmail(refreshToken);
        // 그 이메일로 redis에서 토큰값 갖고오고
        String savedToken = refreshTokenRepository.find(email);

        // 또 한번 유효성 검사 해주면서
        if(savedToken == null || !savedToken.equals(refreshToken)){
            throw new InvalidTokenException();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 모든 유효성 검사 통과하면 이메일값으로 새로운 토큰 값들 만들기
        String newAccessToken = jwtProvider.generateAccessToken(email,user.getRole().name());
        String newRefreshToken = jwtProvider.generateRefreshToken(email);

        // redis에 새로운 refresh token 저장해주기
        refreshTokenRepository.save(email,newRefreshToken, jwtProvider.getRefreshTokenExpiration());

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
