package com.factopia.member.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Member {
    private String memberNo;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberPhone;
    private String memberEmail;
    private int memberRank;
    private int memberAuthority;
    private String enterpriseNo;

}