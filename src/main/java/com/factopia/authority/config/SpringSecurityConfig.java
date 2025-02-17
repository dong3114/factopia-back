package com.factopia.authority.config;

import com.factopia.authority.util.JwtAuthenticationFiler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SpringSecurityConfig {

    private final JwtAuthenticationFiler jwtAuthenticationFiler;

    public SpringSecurityConfig(JwtAuthenticationFiler jwtAuthenticationFiler) {
        this.jwtAuthenticationFiler = jwtAuthenticationFiler;
    }

    // ✅ 인증 예외 URL 리스트
    private static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/health",
            "/actuator/info",
            "/api/auth/**",
            "/api/member/register/**",
            "/api/member/login"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("🔧 Spring Security 설정 시작");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST.toArray(new String[0])).permitAll() // 🔹 배열로 변환 후 적용
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "요청하신 API가 존재하지 않습니다.");
                        })
                )
                .addFilterBefore(jwtAuthenticationFiler, UsernamePasswordAuthenticationFilter.class);

        System.out.println("✅ Spring Security 설정 완료");
        return http.build();
    }

    public static List<String> getAuthWhiteList() {
        return AUTH_WHITELIST;
    }

    // ✅ CORS 설정 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 URL 추가
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
