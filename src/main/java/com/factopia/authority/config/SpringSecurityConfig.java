package com.factopia.authority.config;

import com.factopia.authority.util.JwtAuthenticationFiler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SpringSecurityConfig {

    private final JwtAuthenticationFiler jwtAuthenticationFiler;

    public SpringSecurityConfig(JwtAuthenticationFiler jwtAuthenticationFiler) {
        this.jwtAuthenticationFiler = jwtAuthenticationFiler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("🔧 Spring Security 설정 시작");

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {

                    // ✅ 인증 없이 접근 가능해야 하는 API
                    auth.requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/actuator/health",
                            "/actuator/info",
                            "/api/auth/**",
                            "/api/register/**",
                            // 추후 삭제
                            "/api/factory/**"

                    ).permitAll();

                    // 🔒 그 외 모든 요청은 인증 필요
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFiler, UsernamePasswordAuthenticationFilter.class);

        System.out.println("✅ Spring Security 설정 완료");
        return http.build();
    }

    // ✅ CORS 설정 추가
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 URL 추가
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
