package com.factopia.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Member {
    final String memberNo;
    final String memberId;
    final String memberPw;
    final String memberName;
    final String memberPhone;
    final String memberEmail;
    final int memberAuthority;
    final int memberRank;
    final String enterpriseNo;
}
