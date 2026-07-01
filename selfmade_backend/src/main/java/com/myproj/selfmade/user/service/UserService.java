package com.myproj.selfmade.user.service;

import com.myproj.selfmade.company.entity.Company;
import com.myproj.selfmade.company.repository.CompanyRepository;
import com.myproj.selfmade.global.exception.DuplicateBusinessNumberException;
import com.myproj.selfmade.global.exception.DuplicateEmailException;
import com.myproj.selfmade.global.exception.InvalidPasswordException;
import com.myproj.selfmade.global.exception.UserNotFoundException;
import com.myproj.selfmade.global.jwt.JwtProvider;
import com.myproj.selfmade.global.jwt.RefreshTokenRepository;
import com.myproj.selfmade.user.dto.request.LoginRequestDto;
import com.myproj.selfmade.user.dto.request.SellerSignUpRequestDto;
import com.myproj.selfmade.user.dto.request.SignUpRequestDto;
import com.myproj.selfmade.user.dto.response.TokenResponseDto;
import com.myproj.selfmade.user.dto.response.UserResponseDto;
import com.myproj.selfmade.user.entity.User;
import com.myproj.selfmade.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository  companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입(일반 회원)
    @Transactional
    public UserResponseDto signUp(SignUpRequestDto request) {
        // 이메일 중복 검사 한번 해주고
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException();
        }

        // 저장되는 비밀번호는 인코딩 해주고
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 인코딩된 비밀번호로 User 객체 만들어서
        User user = request.toEntity(encodedPassword);
        // 저장
        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    // 회원가입(seller 회원가입)
    public UserResponseDto sellerSignup(SellerSignUpRequestDto request) {
        // 이메일 중복 검사
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException();
        }

        // 사업자번호 중복 검사
        if(companyRepository.existsByBusinessNumber(request.getBusinessNumber())){
            throw new DuplicateBusinessNumberException();
        }

        // User 저장
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toUserEntity(encodedPassword);
        userRepository.save(user);

        Company company = Company.builder()
                .user(user)
                .name(request.getName())
                .businessNumber(request.getBusinessNumber())
                .representative(request.getRepresentative())
                .address(request.getAddress())
                .build();
        companyRepository.save(company);

        return UserResponseDto.from(user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        // 그니까 요청으로 온 값의 비밀번호와 그 유저의 비밀번호가 일치하지 않으면 예외 처리
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidPasswordException();
        }

        // jwt 로직 추가
        // 로그인 하면 토큰 두개 만들어주고
        String accessToken = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        refreshTokenRepository.save(user.getEmail(), refreshToken, jwtProvider.getRefreshTokenExpiration());

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String email){
        refreshTokenRepository.delete(email);
    }

    // 유저 단건 조회 (email 기반)
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return UserResponseDto.from(user);
    }
}
