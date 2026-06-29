package com.myproj.selfmade.user.controller;

import com.myproj.selfmade.ApiResponse;
import com.myproj.selfmade.user.dto.request.LoginRequestDto;
import com.myproj.selfmade.user.dto.request.SignUpRequestDto;
import com.myproj.selfmade.user.dto.response.TokenResponseDto;
import com.myproj.selfmade.user.dto.response.UserResponseDto;
import com.myproj.selfmade.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@RequestParam String email){
        UserResponseDto data = userService.findByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto request){
        UserResponseDto data = userService.signUp(request);
        return ResponseEntity.status(201).body(ApiResponse.success(data, "회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@Valid @RequestBody LoginRequestDto request){
        TokenResponseDto data = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal String email){
        userService.logout(email);
        return ResponseEntity.noContent().build();
    }

}
