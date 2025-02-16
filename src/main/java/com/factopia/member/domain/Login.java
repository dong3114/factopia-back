package com.factopia.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Login {
    @JsonProperty("memberId")
    private String inputMemberId;
    @JsonProperty("memberPw")
    private String inputMemberPw;
}
