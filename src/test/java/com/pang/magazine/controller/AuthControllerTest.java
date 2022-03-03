package com.pang.magazine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pang.magazine.domain.Member;
import com.pang.magazine.dto.MemberDto;
import com.pang.magazine.repository.MemberRepository;
import com.pang.magazine.security.jwt.JwtProvider;
import com.pang.magazine.service.MemberService;
import com.pang.magazine.util.CustomResponseEntity;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    Gson gson = new Gson();

    @Test
    @DisplayName("회원가입 테스트하기")
    void register() throws Exception{
        // given : Mock객체가 특정 상황에서 해야하는 행위를 정의
        MemberDto.reqRegisterDto dto = MemberDto.reqRegisterDto.builder()
                .username("power")
                .password("123123")
                .check_password("123123")
                .name("fine")
                .build();

        CustomResponseEntity response = CustomResponseEntity.builder()
                .code(HttpStatus.OK)
                .message("회원가입 완료")
                .data(dto)
                .build();


        String res = gson.toJson(response);
        String content = gson.toJson(dto);

        given(memberService.register(dto)).willReturn(res);

        // when : 기대하는 값이 나왔는지
        MockHttpServletResponse response1 = mockMvc.perform(
                post("/api/signup")
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();
        // then
        assertThat(response.getMessage()).isEqualTo("회원가입 완료");
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK);
    }

//    @Test
//    @DisplayName("로그인 테스트하기")
//    public void loginTest() throws Exception {
//        //given
//        MemberDto.reqLoginDto dto = MemberDto.reqLoginDto.builder()
//                .username("power")
//                .password("123123")
//                .build();
//        Member member = Member.builder()
//                .username("power")
//                .password("123123")
//                .name("fine")
//                .build();
//
//        CustomResponseEntity response = CustomResponseEntity.builder()
//                .accessToken("token")
//                .code(HttpStatus.OK)
//                .message("회원가입 완료")
//                .data(dto)
//                .build();
//        String req = gson.toJson(dto);
//        given(memberRepository.save(member));
//        given(memberService.login(dto)).willReturn(response);
//
//        //when
//        MockHttpServletResponse response1 = mockMvc.perform(
//                post("/api/signin")
//                .content(req)
//                 .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // then
//        assertThat(response.getMessage()).isEqualTo("토큰 돌려주기");
//        assertThat(response.getCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getAccessToken()).isEqualTo("token");
//    }
}
