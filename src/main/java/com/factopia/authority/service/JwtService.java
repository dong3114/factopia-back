package com.factopia.authority.service;

public interface JwtService {
    /**
     * Jwt토큰 생성
     * @param memberNo 회원코드
     * @param authority 회원 권한 등급
     * @return 생성된 JWT 토큰
     */
    String generateToken(String memberNo, String authority);
}
