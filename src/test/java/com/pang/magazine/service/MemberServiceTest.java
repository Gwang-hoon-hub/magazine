package com.pang.magazine.service;

import com.pang.magazine.dto.MemberDto;
import com.pang.magazine.util.CustomResponseEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 서비스 테스트")
    public void registerTest(){
        //given
        MemberDto.reqRegisterDto dto = MemberDto.reqRegisterDto.builder()
                .username("pionepine")
                .password("123123")
                .check_password("123123")
                .name("power")
                .build();
        //when
        String msg = memberService.register(dto);

        //then
        Assertions.assertThat(msg).isEqualTo("success");
    }

    @Test
    @DisplayName("로그인 서비스 테스트")
    public void loginTest(){
        //given
        registerTest();
        MemberDto.reqLoginDto dto = MemberDto.reqLoginDto.builder()
                .username("pionepine")
                .password("123123")
                .build();
        //when
        CustomResponseEntity msg = memberService.login(dto);
        //tehn
        Assertions.assertThat(msg.getMessage()).isEqualTo("토큰 돌려주기");
        Assertions.assertThat(msg.getCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(msg.getAccessToken()).isNotNull();
    }

}
