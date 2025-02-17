package com.factopia.authority.util;

import com.factopia.authority.config.SpringSecurityConfig;
import com.factopia.dbenum.Role;
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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFiler extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final FilterExceptionHandler filterExceptionHandler;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public JwtAuthenticationFiler(JwtUtil jwtUtil,
                                  FilterExceptionHandler filterExceptionHandler,
                                  RequestMappingHandlerMapping requestMappingHandlerMapping){
        this.jwtUtil = jwtUtil;
        this.filterExceptionHandler = filterExceptionHandler;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
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

        // ✅ 존재하지 않는 컨트롤러 요청을 404로 처리
        if (!isExistingAPI(requestURI)) { // 존재하지 않는 API 요청
            System.out.println("🚨 [JwtAuthenticationFilter] 존재하지 않는 API 요청 - 404 Not Found");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "API 경로가 존재하지 않습니다.");
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

        Role level = jwtUtil.extractLevel(token);
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

    /**
     * ✅ 존재하는 API인지 확인하는 메서드
     * Spring MVC에 등록된 컨트롤러의 매핑 정보를 조회하여 확인
     */
    private boolean isExistingAPI(String requestURI) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            if (mappingInfo.getPathPatternsCondition() != null) {
                for (org.springframework.web.util.pattern.PathPattern pattern : mappingInfo.getPathPatternsCondition().getPatterns()) {
                    if (requestURI.matches(pattern.getPatternString().replace("**", ".*"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
