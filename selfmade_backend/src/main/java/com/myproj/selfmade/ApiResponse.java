package com.myproj.selfmade;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @apiNote
 * 공통 응답을 만들기 위한 클래스
 * 성공은 200으로, 실패는 커스텀 예외로 처리
 */

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = "요청이 성공했습니다.";
        response.data = data;
        return response;
    }

    // 성공 메세지 커스텀 하고 싶을 때 사용
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.data = null;
        return response;
    }
}
