package com.factopia.authority.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityCongfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // 공용 경로 허용
                    EndPointConfig.PERMIT_ALL_ENDPOINT.forEach(endpoint ->
                            auth.requestMatchers(endpoint).permitAll()
                    );

                    // 레벨별 경로 설정
                    EndPointConfig.MEMBER_ENDPOINT.forEach(endpoint ->
                            auth.requestMatchers(endpoint).hasAnyRole("USER", "COMPANY_MANAGER", "COMPANY_OWNER", "ADMIN")
                    );

                    EndPointConfig.COMPANY_MANAGER_ENDPOINT.forEach(endpoint ->
                            auth.requestMatchers(endpoint).hasAnyRole("COMPANY_MANAGER", "COMPANY_OWNER", "ADMIN")
                    );

                    EndPointConfig.COMPANY_OWNER_ENDPOINT.forEach(endpoint ->
                            auth.requestMatchers(endpoint).hasAnyRole("COMPANY_OWNER", "ADMIN")
                    );

                    EndPointConfig.ADMIN_ENDPOINT.forEach(endpoint ->
                            auth.requestMatchers(endpoint).hasRole("ADMIN")
                    );

                    // 그 외 모든 요청은 인증 필요
                    auth.anyRequest().authenticated();
                });

        return http.build();
    }
}
