package com.factopia.authority.util;

import com.factopia.authority.config.SpringSecurityConfig;
import com.factopia.handler.exception.FilterExceptionHandler;
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
import java.util.List;

@Component
public class JwtAuthenticationFiler extends OncePerRequestFilter {
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
        System.out.println("🛠 [JwtAuthenticationFilter] 요청 시작: " + request.getMethod() + " " + request.getRequestURI());

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // ✅ OPTIONS 요청이면 필터를 통과시킴 (CORS 해결)
        if (method.equalsIgnoreCase("OPTIONS")) {
            System.out.println("🛠 [JwtAuthenticationFilter] OPTIONS 요청 필터 통과");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        List<String> authWhiteList = SpringSecurityConfig.getAuthWhiteList();

        // ✅ 인증이 필요 없는 요청이면 필터 통과
        if (authWhiteList.stream().anyMatch(pattern -> requestURI.matches(pattern.replace("**", ".*")))) {
            System.out.println("🛠 [JwtAuthenticationFilter] 인증 예외 경로 접근: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null) {
            System.out.println("🚨 [JwtAuthenticationFilter] 인증 실패 - Authorization 헤더 없음");
        } else {
            System.out.println("🔎 [JwtAuthenticationFilter] Authorization 헤더 확인: " + authorizationHeader);
        }

        // 🔹 헤더 검사
        if (authorizationHeader == null || !jwtUtil.hasValidHeader(authorizationHeader)) {
            System.out.println("🚨 [JwtAuthenticationFilter] 인증 실패 - 유효하지 않은 헤더");
            filterExceptionHandler.handleAuthenticationFailure(response, "토큰이 없거나 Bearer 타입이 아닙니다.");
            return;
        }

        String token = authorizationHeader.replace("Bearer", "");
        System.out.println("🔎 [JwtAuthenticationFilter] 토큰 추출 완료: " + token);

        // 2. 유효성 검증
        if(!jwtUtil.validateToken(token)){
            System.out.println("🚨 [JwtAuthenticationFilter] 인증 실패 - 잘못된 토큰");
            filterExceptionHandler.handleAuthenticationFailure(response, "인증 실패");
            return;
        }

        int level = jwtUtil.extractLevel(token);
        String memberNo = jwtUtil.extractMemberNo(token);

        System.out.println("✅ [JwtAuthenticationFilter] 인증 성공 - 사용자: " + memberNo + " / Level: " + level);

        // 3. 인증 객체 설정 및 정보 추출
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
