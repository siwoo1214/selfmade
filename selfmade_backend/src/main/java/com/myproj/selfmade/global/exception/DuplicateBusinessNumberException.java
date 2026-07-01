package com.myproj.selfmade.global.exception;

import org.springframework.http.HttpStatus;

public class DuplicateBusinessNumberException extends BusinessException{
    public DuplicateBusinessNumberException() {
        super("이미 등록된 사업자번호입니다.", HttpStatus.CONFLICT);
    }
}
