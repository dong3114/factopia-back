package com.factopia.member.controller;

import com.factopia.authority.util.JwtUtil;
import com.factopia.member.domain.Member;
import com.factopia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> insertMember(@RequestBody Member member){
        // 1. 필수 값 누락 검증
        validateRequiredFields(member);
        // 성공 1, 실패 0
        int result = memberService.insertMember(member);
        if(result> 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            throw new IllegalArgumentException("회원가입 실패");
        }
    }

    @PutMapping("/validate/id")
    public ResponseEntity<Map<String, Object>> validateId(@RequestParam String memberId){
        System.out.println("아이디 유효성검증: " + memberId) ;
        Map<String, Object> result = new HashMap<>();

        if(memberId == null || memberId.isEmpty()){
            throw new IllegalArgumentException("ID를 입력해야합니다.");
        }

        //  중복된 아이디 없을떄
        boolean isValid = memberService.validateId(memberId);
        System.out.println("isVlid: " + isValid);

        result.put("memberId", memberId);
        result.put("isValid", isValid);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/validate/email")
    public ResponseEntity<Map<String, Object>> validateEmail(@RequestParam String memberEmail){
        Map<String, Object> result = new HashMap<>();

        if(memberEmail == null || memberEmail.isEmpty()){
            throw new IllegalArgumentException("이메일을 입력해야합니다.");
        }

        boolean isValid = memberService.validateEmail(memberEmail);

        result.put("memberEmail", memberEmail);
        result.put("isValid", isValid);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/validate/phone")
    public ResponseEntity<Map<String, Object>> validatePhone(@RequestParam String memberPhone){
        Map<String, Object> result = new HashMap<>();

        if(memberPhone == null || memberPhone.isEmpty()){
            throw new IllegalArgumentException("핸드폰번호를 입력해야합니다.");
        }

        boolean isValid = memberService.validatePhone(memberPhone);

        result.put("memberPhone", memberPhone);
        result.put("isValid", isValid);

        return ResponseEntity.ok(result);
    }

    // 필수 값 검증 메서드 (유효성 검증 제거)
    private void validateRequiredFields(Member member) {
        if (member.getMemberId() == null ||
                member.getMemberPw() == null ||
                member.getMemberName() == null ||
                member.getMemberPhone() == null ||
                member.getMemberEmail() == null ||
                member.getEnterpriseNo() == null) {
            throw new IllegalArgumentException("회원가입 필수 정보 누락");
        }
    }
}
