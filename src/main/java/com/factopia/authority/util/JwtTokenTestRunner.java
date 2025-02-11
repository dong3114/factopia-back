package com.factopia.authority.util;

import com.factopia.authority.domain.JwtToken;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenTestRunner implements CommandLineRunner {

    private final JwtToken jwtToken;

    public JwtTokenTestRunner(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("JWT Expiration: " + jwtToken.getExpiration());
        System.out.println("JWT Issuer: " + jwtToken.getIssuer());
        System.out.println("JWT Token Prefix: " + jwtToken.getTokenPrefix());
    }
}
