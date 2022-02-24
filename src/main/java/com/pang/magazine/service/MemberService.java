package com.pang.magazine.service;

import com.pang.magazine.domain.Member;
import com.pang.magazine.dto.MemberDto;
import com.pang.magazine.exception.exceptionclass.NotRegisterException;
import com.pang.magazine.exception.exceptionclass.NotSamePwdException;
import com.pang.magazine.repository.LikeRepository;
import com.pang.magazine.repository.MemberRepository;
import com.pang.magazine.util.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;


    @Transactional(readOnly = true)
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
        if(!dto.getPassword().equals(dto.getPasswordCheck())){
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
}
