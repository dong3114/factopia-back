package com.factopia.authority.config;

import java.util.List;

public class EndPointConfig {

    // 공용 경로
    public static final List<String> PERMIT_ALL_ENDPOINT = List.of(
            "/swagger-ui/**",        // Swagger UI 경로
            "/v3/api-docs/**",       // Swagger API Docs 경로
            "/actuator/health",      // Actuator - Health Endpoint
            "/actuator/info",        // Actuator - Info Endpoint
            "/api/auth/**"           // 인증 관련 경로
    );

    // 레벨별 경로
    public static final List<String> MEMBER_ENDPOINT = List.of("/api/member/**");
    public static final List<String> COMPANY_MANAGER_ENDPOINT = List.of("/api/manager/**");
    public static final List<String> COMPANY_OWNER_ENDPOINT = List.of("/api/owner/**");
    public static final List<String> ADMIN_ENDPOINT = List.of("/api/admin/**");
}
