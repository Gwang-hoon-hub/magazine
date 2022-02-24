package com.pang.magazine.security;

import com.pang.magazine.exception.AuthFailureHandler;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

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


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }// Register HttpSessionEventPublisher

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
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
                .ignoringAntMatchers( "/api/**");

        // 최대 세션 수 설정
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/duplicated-login")
                .sessionRegistry(sessionRegistry());

        http
                .authorizeRequests()
                    .antMatchers("/api/**").permitAll()
                    .anyRequest().permitAll()
//                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                //  로그인 처리 : POST방식 (/signin)
                    .loginPage("/api/signin")
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
