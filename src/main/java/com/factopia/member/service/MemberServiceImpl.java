package com.factopia.member.service;

import com.factopia.authority.util.JwtUtil;
import com.factopia.dbenum.Role;
import com.factopia.member.domain.Login;
import com.factopia.member.domain.Member;
import com.factopia.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public int insertMember(Member member){
        // 1. 필수 입력값 검증
        requestVaild(member);
        // 2. 중복 검사
        validateMember(member);
        // 3. 회원 등록 (성공 1, 실패 0)
        return memberMapper.insertMember(member);
    }

    // 필수 입력값 검증
    private void requestVaild(Member member){
        System.out.println("서비스: requestVaild");

        if(member.getMemberId() == null ||
           member.getMemberPw() == null ||
           member.getMemberName() == null ||
           member.getMemberEmail() == null ||
           member.getMemberPhone() == null){
           throw new IllegalArgumentException("회원가입 필수 정보 누락");
        }
    }

    // 회원코드를 통해 회원 정보 취득
    @Override
    public String login(Login loginRequset){
        Member member = memberMapper.login(loginRequset.getInputMemberId(), loginRequset.getInputMemberPw());

        if(member == null){
            throw new IllegalArgumentException("아이디 또는 비밀번호 확인해주세요.");
        }

        String memberNo = member.getMemberNo();
        Role role = Role.fromLevel(member.getMemberRank());
        String enterpriseNo = member.getEnterpriseNo();

        return jwtUtil.generateToken(memberNo, role, enterpriseNo);
    }

    // 아이디 유효성 검증
    @Override
    public boolean validateId(String memberId){
        int count = memberMapper.validateId(memberId);
        return memberMapper.validateId(memberId) == 0;
    }

    // 이메일 유효성 검증
    @Override
    public boolean validateEmail(String memberEmail){
        return memberMapper.checkMemberEmail(memberEmail) == 0;
    }

    // 휴대폰 유효성 검증
    @Override
    public boolean validatePhone(String memberPhone){
        return memberMapper.checkMemberPhone(memberPhone) == 0;
    }

    // 중복 검사 로직
    private void validateMember(Member member){
        System.out.println("서비스: validateMember");

        if(!validateId(member.getMemberId())){
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }
        if (!validatePhone(member.getMemberPhone())) {
            throw new IllegalStateException("이미 사용 중인 전화번호입니다.");
        }
        if (!validateEmail(member.getMemberEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }
    }
}
