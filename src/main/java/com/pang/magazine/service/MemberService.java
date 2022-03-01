package com.pang.magazine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pang.magazine.domain.Member;
import com.pang.magazine.dto.MemberDto;
import com.pang.magazine.exception.exceptionclass.NotRegisterException;
import com.pang.magazine.exception.exceptionclass.NotSamePwdException;
import com.pang.magazine.repository.LikeRepository;
import com.pang.magazine.repository.MemberRepository;
import com.pang.magazine.security.UserDetailsImpl;
import com.pang.magazine.security.jwt.JwtProvider;
import com.pang.magazine.util.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Service
public class MemberService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final JwtProvider jwtProvider;


    @Transactional
    public String register(MemberDto.reqRegisterDto dto){
        // 이미 존재하는 아이디이면 예외처리
        if(memberRepository.existsByUsername(dto.getUsername())){
            throw new NotRegisterException("이미 존재하는 아이디입니다.1");
        }
        // 비밀번호에 username이 있으면 비밀번호 안됨
        if(dto.getPassword().contains(dto.getUsername())){
            throw new NotRegisterException("비밀번호 양식이 맞지 않음.1");
        }
        // 비밀번호 재확인하기
        if(!dto.getPassword().equals(dto.getCheck_password())){
            throw new NotRegisterException("비밀번호가 일치하지 않습니다.1");
        }

        String pwd = passwordEncoder.encode(dto.getPassword());
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(pwd)
                .name(dto.getName())
                .build();
        Member saved = memberRepository.save(member);
        String msg = "";
        if (member.equals(saved)){
            msg = "success";
        }
        return msg;
    }

    public CustomResponseEntity login(MemberDto.reqLoginDto dto){
        Member member = Optional
                .ofNullable(memberRepository.findByUsername(dto.getUsername()))
                .orElseThrow(() -> new UsernameNotFoundException("(해당 유저는 없습니다.) user can't find"));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(member);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication.getName());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", token);

//        response.setContentType(APPLICATION_JSON_VALUE);

        return CustomResponseEntity.builder()
                .headerData(tokens)
                .data(null)
                .code(HttpStatus.OK)
                .message("토큰 돌려주기")
                .build();
    }

}
