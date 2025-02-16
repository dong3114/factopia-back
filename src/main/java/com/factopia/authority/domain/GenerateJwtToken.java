package com.factopia.authority.domain;

import lombok.*;

@Getter
@AllArgsConstructor
public class GenerateJwtToken {
    private final String token;
    private final String memberNo;
    private final String enterpriseNo;
    private final long expires;
}
