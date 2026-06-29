package com.myproj.selfmade.global.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND);
    }
}