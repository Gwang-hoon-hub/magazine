package com.pang.magazine.security;

import com.pang.magazine.exception.AuthFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean//UserDetailsService에서 사용자 세부 정보를 검색하는 AuthenticationProvider 구현입니다. 로그인시 ~
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider bean = new DaoAuthenticationProvider();
        bean.setHideUserNotFoundExceptions(false); //이 부분을 설정해줘야 아이디가 불일치 에러 처리 가능
        bean.setUserDetailsService(userDetailsService);
        bean.setPasswordEncoder(this.encodePassword());
        return bean;
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .ignoringAntMatchers( "/api/**" , "/user/**");

        http
                .authorizeRequests()
                    .antMatchers("/api/**").permitAll()
                    .anyRequest().permitAll()
//                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                //  로그인 처리 : POST방식 (/signin)
                    .loginProcessingUrl("/api/signin")
                    .failureHandler(new AuthFailureHandler())
                    .defaultSuccessUrl("/api/singinSuccess")
                    .permitAll()
                // defaultSuccessUrl -> 성공 시 가야하는 url / failureUrl -> 실패 시 가야하는 url
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .permitAll();
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}
