package com.pang.magazine.controller;

import com.pang.magazine.dto.MemberDto;
import com.pang.magazine.service.MemberService;
import com.pang.magazine.util.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    //회원가입
    @PostMapping("/api/signup")
    public ResponseEntity register(@Valid @RequestBody MemberDto.reqRegisterDto dto){
        memberService.register(dto);
        CustomResponseEntity response = CustomResponseEntity.builder()
                .code(HttpStatus.OK)
                .message("회원가입 완료")
                .build();
        System.out.println("리스폰스엔터티 보여주기" + response.responseNotData().toString());
        return response.responseNotData();
    }

    // 로그인은 securityConfig 에서 처리하기
    @GetMapping("/api/singinSuccess")
    public ResponseEntity singin(Principal principal) throws AuthenticationException {
        CustomResponseEntity response;
        // 유저 정보가 없는 경우 => null
        if(principal != null){
             throw new AuthenticationException("이미 로그인 되어있는 회원입니다.");
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}

