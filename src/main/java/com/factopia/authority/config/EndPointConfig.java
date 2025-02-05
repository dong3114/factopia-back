package com.factopia.authority.config;

import java.util.List;

public class EndPointConfig {

    // ✅ 인증 없이 접근 가능한 엔드포인트 (회원가입 관련 API 추가)
    public static final List<String> PERMIT_ALL_ENDPOINT = List.of(
            "/swagger-ui/**",        // Swagger UI 경로
            "/v3/api-docs/**",       // Swagger API Docs 경로
            "/actuator/health",      // Actuator - Health Endpoint
            "/actuator/info",        // Actuator - Info Endpoint
            "/api/auth/**",          // 인증 관련 경로
            "/api/members/validate/**", // ✅ 회원가입 시 유효성 검사 관련 API
            "/api/members/register"  // ✅ 회원가입 API도 인증 없이 접근 가능하도록 설정
    );

    // 🔒 레벨별 접근 설정
    public static final List<String> MEMBER_ENDPOINT = List.of("/api/member/**");
    public static final List<String> COMPANY_MANAGER_ENDPOINT = List.of("/api/manager/**");
    public static final List<String> COMPANY_OWNER_ENDPOINT = List.of("/api/owner/**");
    public static final List<String> ADMIN_ENDPOINT = List.of("/api/admin/**");
}
