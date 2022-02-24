package com.pang.magazine.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotValidResponse {
    private String field; // 에러가 난 필드
    private Object value; // 입력으로 들어온 값
    private String message; // 보낼 메세지

//    @Builder
//    public NotValidResponse(String field, Object value, String message) {
//        this.field = field;
//        this.value = value;
//        this.message = message;
//    }
}
