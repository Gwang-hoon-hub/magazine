package com.pang.magazine.controller;

import com.pang.magazine.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
//@AutoConfigureWebMvc    // MockMvc를 Builder 없이 주입받을 수 잇음
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Test
    @DisplayName("회원가입 테스트하기")
    void register() throws Exception{
        // given : Mock객체가 특정 상황에서 해야하는 행위를 정의

        // when : 기대하는 값이 나왔는지

        // then
    }
}
