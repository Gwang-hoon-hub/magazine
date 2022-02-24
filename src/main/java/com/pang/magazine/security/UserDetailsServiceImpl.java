package com.pang.magazine.security;

import com.pang.magazine.domain.Member;
import com.pang.magazine.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

         Member member = Optional
                 .ofNullable(memberRepository.findByUsername(username))
                 .orElseThrow(() -> new UsernameNotFoundException("user can't find"));

        return new UserDetailsImpl(member);
    }
}

