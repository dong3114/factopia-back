package com.factopia.authority.util;

import com.factopia.handler.exception.FilterExceptionHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFiler extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;
    private final FilterExceptionHandler filterExceptionHandler;

    public JwtAuthenticationFiler(JwtUtil jwtUtil, FilterExceptionHandler filterExceptionHandler){
        this.jwtUtil = jwtUtil;
        this.filterExceptionHandler = filterExceptionHandler;
    }

    /**
     * JWT 인증필터
     * - 토큰의 유효성 확인은 Level, MemberNo로 진행
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     *                 - 2025.01.01 기준
     *                 - 권한에 대한 응답 뿐이므로 401에러만 반환
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizetionHeader = request.getHeader("Authorization");

        // 토큰이 유무 체크, 형식 체크


        // 유효성 검증
        if(!jwtUtil.vaildateToken(authorizetionHeader)){
            filterExceptionHandler.handleAuthenticationFailure(response, "인증 실패");
        }

        if(authorizetionHeader == null && !authorizetionHeader.startsWith("Bearer ")){
            filterExceptionHandler.handleAuthenticationFailure(response, "토큰이 없거나 Bearer 타입이 아닙니다.");
            return;
        }



        int level = jwtUtil.extractLevel(authorizetionHeader);
        String memberNo = jwtUtil.extractMemberNo(authorizetionHeader);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberNo,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_LEVEL_" + level))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }


}
