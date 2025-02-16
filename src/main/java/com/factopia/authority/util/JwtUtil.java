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
     * ✅ JWT 갱신 (토큰 만료시간 연장)
     */
    public String refreshToken(String oldToken) {
        if(!validateToken(oldToken)) {
            throw new RuntimeException("토큰이 유효하지 않습니다.");
        }

        String memberNo = extractMemberNo(oldToken);
        String enterpriseNo = extractEnterpriseNo(oldToken);
        Role role = extractLevel(oldToken);

        return generateToken(memberNo, role, enterpriseNo);
    }

    /**
     * ✅ JWT 검증 및 만료 확인
     */
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
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
    public Role extractLevel(String token){
        Role role = Role.fromLevel(getClaims(token).get("level", Integer.class));
        return role;
    }

    /**
     * JWT에서 회원코드 추출
     */
    public String extractMemberNo(String token){
        return getClaims(token).getSubject();
    }

    public String extractEnterpriseNo(String token){
        return getClaims(token).get("e_no", String.class);
    }

    /**
     * 토큰 만료 시간 반환
     */
    public long getExpirationTime() {
        return jwtToken.getExpiration();
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
