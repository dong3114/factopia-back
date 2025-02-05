package com.factopia.member.service;

import com.factopia.member.domain.Member;

import java.util.List;

public interface MemberService {
    int insertMember(Member member);
    Member getMemberNo(String memberNo);

    // 유효성 검증
    boolean validateId(String memberId);
    boolean validatePhone(String memberPhone);
    boolean validateEmail(String memberEmail);

}
