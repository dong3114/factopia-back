package com.factopia.factory.domain;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더사용강제
@Builder
public class FactorySite {
    // 공장부지
    private String factoryNo; // 공장부지코드
    private String enterpriseNo; // 기업코드

    private Double totalWidth; // 공장전체 'x축'
    private Double totalHeight; // 공장전체 'y축'
    private Double totalDepth; // 공장전체 'z축'

    @Builder.Default
    private Timestamp createTime = new Timestamp(System.currentTimeMillis()); // 현재 시간을 기본값으로 한다.

    @Builder.Default
    private Timestamp updateTime = null; // 수정 시간은 기본값 null

    private String factorySiteName; // 공장부지 이름
}
