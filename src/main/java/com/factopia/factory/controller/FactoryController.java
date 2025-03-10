package com.factopia.factory.controller;

import com.factopia.authority.util.JwtUtil;
import com.factopia.factory.domain.FactoryDataResponse;
import com.factopia.factory.service.FactoryService;
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
@RequestMapping("/api/factories")
public class FactoryController {
    @Autowired
    private final JwtUtil jwtUtil;
    private final FactoryService factoryService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> factoryAllData(
            @RequestHeader(value = "Authorization", required = false) String token){
        if(!jwtUtil.hasValidHeader(token)){
            throw new SecurityException("유효하지 않은 토큰입니다.");
        }

        String enterpriseNo = jwtUtil.extractEnterpriseNo(token);
        FactoryDataResponse factories = factoryService.factoryAllData(enterpriseNo);
        System.out.println("공장" + factories);

        // 응답 객체
        Map<String, Object> response = new HashMap<>();
        if(factories == null){
            response.put("message", "등록된 공장이 없습니다.");
            response.put("factories", List.of());
            return ResponseEntity.ok(response);
        }

        response.put("message", "공장 조회 성공");
        response.put("factories", factories);

        return ResponseEntity.ok(response);
    }

}
