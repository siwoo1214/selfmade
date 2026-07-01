package com.myproj.selfmade.global.exception;

import org.springframework.http.HttpStatus;

public class CompanyNotFoundException extends BusinessException{
    public CompanyNotFoundException() {
        super("존재하지 않는 판매자입니다.", HttpStatus.NOT_FOUND);
    }
}
