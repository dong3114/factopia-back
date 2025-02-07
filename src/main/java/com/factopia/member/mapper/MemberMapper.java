package com.factopia.member.mapper;

import com.factopia.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    /**
     * 회원 가입
     * @param member 회원 객체
     * @return 삽입 성공 1, 실패 0
     */
    int insertMember(Member member);

    /**
     * 회원 번호로 회원 조회
     * @param memberNo 회원 번호
     * @return 회원 정보
     */
    Member findMemberNo(@Param("memberNo") String memberNo);

    /**
     * 회원가입 유효성검증
     */
    int validateId(@Param("memberId")String memberId);
    int checkMemberPhone(@Param("memberPhone")String memberPhone);
    int checkMemberEmail(@Param("memberEmail")String memberEmail);
}

