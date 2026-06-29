package com.myproj.selfmade.global.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException() {
        super("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}