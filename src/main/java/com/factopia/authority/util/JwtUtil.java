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
     * @param token
     * @return 권한레벨
     */
    public int extractLevel(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtToken.getSecret())
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody();
            return claims.get("level", Integer.class);
        } catch (ExpiredJwtException e){
            throw new RuntimeException("JWT가 만료되었습니다.", e);
        } catch (SignatureException e){
            throw new RuntimeException("JWT 서명이 유효하지 않습니다.", e);
        } catch (Exception e){
            throw new RuntimeException("JWT가 유효하지 않습니다.", e);
        }
    }

    public String extractMemberNo(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtToken.getSecret())
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException e){
            throw new RuntimeException("JWT가 만료되었습니다.", e);
        } catch (SignatureException e){
            throw new RuntimeException("JWT 서명이 유효하지 않습니다.", e);
        } catch (Exception e){
            throw new RuntimeException("JWT가 유효하지 않습니다.", e);
        }
    }
}
