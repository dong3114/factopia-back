package com.factopia.member.service;

import com.factopia.member.domain.Member;
import com.factopia.member.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberMapper memberMapper){
        this.memberMapper = memberMapper;
    }

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
    public Member getMemberNo(String memberNo){
        Member member = memberMapper.findMemberNo(memberNo);
        if(member == null){
            throw new IllegalArgumentException("해당 회원 정보가 없습니다.");
        }
        return member;
    }

    // 아이디 유효성 검증
    @Override
    public boolean validateId(String memberId){
        System.out.println("아이디 유효성확인 서비스로직 실행");
        int count = memberMapper.validateId(memberId);
        System.out.println("카운트" + count);
        return memberMapper.validateId(memberId) == 0;
    }

    // 이메일 유효성 검증
    @Override
    public boolean validateEmail(String memberEmail){
        return !memberMapper.checkMemberEmail(memberEmail);
    }

    // 휴대폰 유효성 검증
    @Override
    public boolean validatePhone(String memberPhone){
        return !memberMapper.checkMemberPhone(memberPhone);
    }

    // 중복 검사 로직
    private void validateMember(Member member){
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
