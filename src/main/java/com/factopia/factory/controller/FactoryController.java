package com.factopia.factory.controller;

import com.factopia.authority.util.JwtUtil;
import com.factopia.factory.service.FactoryService;
import com.factopia.handler.exception.ApiExceptionHandler;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fatories")
public class FactoryController {
    @Autowired
    JwtUtil jwtUtil;
    FactoryService factoryService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> factoryAllData(
            @RequestHeader(value = "Authorization", required = false) String token){
        if(!jwtUtil.hasValidHeader(token)){
            throw new SecurityException("유효하지 않은 토큰입니다.");
        }

        String enterpriseNo = jwtUtil.extractEnterpriseNo(token);
        List<String> factorise = factoryService.getAllFactoryNo(enterpriseNo);

        // 응답 객체
        Map<String, Object> response = new HashMap<>();

        if(factorise.isEmpty()){
            response.put("message", "등록된 공장이 없습니다.");
            response.put("factorise", List.of());
            return ResponseEntity.ok(response);
        }
        response.put("message", "공장 조회 성공");
        response.put("factorise", factorise);

        return ResponseEntity.ok(response);
    }

}
