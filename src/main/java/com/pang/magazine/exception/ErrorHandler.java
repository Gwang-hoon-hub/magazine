package com.pang.magazine.exception;

import com.pang.magazine.exception.exceptionclass.DoubleLoginException;
import com.pang.magazine.exception.exceptionclass.NotRegisterException;
import com.pang.magazine.exception.exceptionclass.NotSamePwdException;
import com.pang.magazine.exception.exceptionclass.NotUpdateWriterException;
import com.pang.magazine.exception.response.ErrorResponse;
import com.pang.magazine.exception.response.NotValidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class ErrorHandler {

    private ErrorResponse response;

    @ExceptionHandler(NotRegisterException.class)
    public ResponseEntity NotRegisterException(NotRegisterException e){
            response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotSamePwdException.class)
    public ResponseEntity NotSamePwdException(NotSamePwdException e){
        response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    //  Bean Validation에서 터지는 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public NotValidResponse notValid(MethodArgumentNotValidException e){
        // 에러가 난 입력값이 여러개여도 하나씩만 보여준다.
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        return NotValidResponse.builder()
                .field(fieldError.getField())               // 오류난 필드명
                .value(fieldError.getRejectedValue())       // 거절된 입력 값
                .message(fieldError.getDefaultMessage())    // 설정한 메세지
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity NotAuthentication(AuthenticationException e){
        response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoubleLoginException.class)
    public ResponseEntity DoubleLoginException(DoubleLoginException e){
        response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
