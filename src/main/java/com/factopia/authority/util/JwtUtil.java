package com.factopia.authority.util;

import com.factopia.authority.domain.JwtToken;
import com.factopia.dbenum.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final JwtToken jwtToken;

    public JwtUtil(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    /**
     * 토큰 발급 정보
     * @param memberNo 회원코드
     * @param role 열거형 권한 정보
     * @return
     */
    public String generateToken(String memberNo, Role role){
        return Jwts.builder()
                .setSubject(memberNo)
                .claim("level", role.getLevel())
                .setIssuer(jwtToken.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtToken.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtToken.getSecret())
                .compact();
    }

    /**
     * JWT에서 권한 등급으로 추출
     */
    public int extractLevel(String token){
        return getClaims(token).get("level", Integer.class);
    }

    /**
     * JWT에서 회원코드 추출
     */
    public String extractMemberNo(String token){
        return getClaims(token).getSubject();
    }

    /**
     * 토큰의 유효성 검사
     * @param token
     * @return
     */
    public boolean vaildateToken(String token){
        try {
            getClaims(token);
            return true;
        } catch (Exception e){
            throw new SecurityException("토큰 검증 실패", e);
        }
    }

    /**
     * 토큰 파싱 공통 메서드
     * @param token 
     * @return 토큰 파싱 데이터
     */
    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtToken.getSecret())
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody();
    }
}
