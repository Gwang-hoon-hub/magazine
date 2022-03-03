package com.pang.magazine.security;

import com.pang.magazine.security.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 적용
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    private final JwtProvider jwtProvider;
    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // cors 필터 사용 /  csrf, 세션 disable하기
        http    .addFilterBefore(new CustomAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);


        http
                .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/signin").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/signup").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/posts").permitAll()
                    .anyRequest().authenticated()
                    .and()
//                .formLogin()
//                .loginProcessingUrl("/api/signin")
//                .successHandler(new LoginSucessHandler(jwtProvider))
//                .failureHandler(new AuthFailureHandler())
//                .and()
                .cors();


//                .apply(new JwtSecurityConfig(jwtProvider)); // jwt필터를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스 적용
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}
