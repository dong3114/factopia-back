package com.factopia.member.domain;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Login {
    private String inputMemberId;
    private String inputMemberPw;
}
