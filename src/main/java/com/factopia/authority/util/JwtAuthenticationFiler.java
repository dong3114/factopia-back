package com.factopia.authority.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFiler extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFiler(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
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

        if(authorizetionHeader != null && authorizetionHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizetionHeader.replace("Bearer ", "");

        try{
            int level = jwtUtil.extractLevel(token);
            String memberNo = jwtUtil.extractMemberNo(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            memberNo,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_LEVEL_" + level))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("인증 실패: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

}
