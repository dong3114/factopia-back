package com.factopia.authority.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtToken {
    private String secret;
    private long expiration;
    private String issuer;
    private String tokenPrefix;
}
