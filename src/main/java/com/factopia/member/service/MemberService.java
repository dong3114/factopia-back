package com.factopia.member.service;

import com.factopia.authority.domain.GenerateJwtToken;
import com.factopia.member.domain.Login;
import com.factopia.member.domain.Member;

import java.util.List;

public interface MemberService {
    int insertMember(Member member);
    GenerateJwtToken login(Login loginRequest);

    // 유효성 검증
    boolean validateId(String memberId);
    boolean validatePhone(String memberPhone);
    boolean validateEmail(String memberEmail);

}
