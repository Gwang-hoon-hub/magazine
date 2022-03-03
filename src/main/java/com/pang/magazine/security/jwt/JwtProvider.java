package com.pang.magazine.security.jwt;

import com.pang.magazine.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    private long accessTokenTime = 1000 * 60 * 5; // 2분
    private long refreshTokenTime = 1000 * 60 * 4; // 4분

    @Autowired
    private UserDetailsServiceImpl userDetailsServicel;

    private String secretKey = "abwieineprmdspowejropsadasdasdasdvsddvsdvasd";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 생성
    public String createToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        log.info("getAuthentication=====");
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsServicel.loadUserByUsername(this.getUserInfo(token));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("해당 유저가 없습니다");

        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public String getUserInfo(String token) {
        log.info("getUserInfo=====");
        try {
            String sub = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            if (sub == null) {
                throw new JwtException("sub == null : 잘못된 형식의 토큰입니다");
            }
            return sub;
        } catch (MalformedJwtException e) {//jwt해석 불가능한경우
            throw new JwtException(" MalformedJwtException:  잘못된 형식의 토큰입니다");
        }
    }

    //request header에서 token값을 가지고온다다
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

    //x토큰 의 유혀성 검증
    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            System.out.println("=======" + claims.getBody().getExpiration() + "=============" +
                    new Date());
            System.out.println(claims.getBody().getExpiration().before(new Date()));
            return !claims.getBody().getExpiration().before(new Date());
        } catch (UnsupportedJwtException e) {
            throw new JwtException("인수가 Claims JWS를 나타내지 않는 경우");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException(" 문자열이 유효한 JWS가 아닌 경우");
        } catch (SignatureException e) {
            throw new SignatureException("JWS 서명 유효성 검사가 실패한 경우");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, claims.getBody(), "만료된 토큰");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("문자열이 null이거나 비어 있거나 공백만 있는 경우");
        }
    }

}