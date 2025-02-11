package com.factopia.authority.util;

import com.factopia.authority.domain.JwtToken;
import com.factopia.dbenum.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final JwtToken jwtToken;

    @Autowired
    public JwtUtil(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    /**
     * 토큰 발급 정보
     * @param memberNo 회원코드
     * @param role 열거형 권한 정보
     * @return
     */
    public String generateToken(String memberNo, Role role, String enterpriseNo){
        return Jwts.builder()
                .setSubject(memberNo)
                .claim("e_no", enterpriseNo)
                .claim("level", role.getLevel())
                .setIssuer(jwtToken.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtToken.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtToken.getSecret())
                .compact();
    }

    /**
     * 토큰의 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 만료됨: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("JWT 형식 오류: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("JWT 서명 오류: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 잘못된 인자: " + e.getMessage());
        }
        return false;
    }

    /**
     * 토큰이 Bearer 형식을 따르는지 검사
     * @param token 토큰의 헤더
     * @return 헤더의 유효성 여부
     */
    public boolean hasValidHeader(String token){
        return token != null && token.startsWith("Bearer ") && token.length() > 7;
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
