package com.myproj.selfmade.user.controller;

import com.myproj.selfmade.ApiResponse;
import com.myproj.selfmade.user.dto.response.TokenResponseDto;
import com.myproj.selfmade.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/tokens")
public class TokenController {

    private final TokenService tokenService;

    // @RequestHeader
    // HTTP 요청의 헤더에서 값을 꺼내는 어노테이션이야.
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponseDto>> reissue(
            @RequestHeader("Refresh-Token") String refreshToken
    ){
        TokenResponseDto data = tokenService.reissue(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
