package com.factopia.factory.domain;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FactorySection {
    private String factorySectionNo; // 사용구역 ID (PK)
    private String factoryZoneNo; // 공장구역 ID (FK)

    private double xStart; // X축 시작 좌표
    private double yStart; // Y축 시작 좌표
    private double zStart; // Z축 시작 좌표

    private double xEnd; // X축 끝 좌표
    private double yEnd; // Y축 끝 좌표
    private double zEnd; // Z축 끝 좌표

    private String usedDescription; // 사용 용도 설명

    @Builder.Default
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    private Timestamp updateTime = null;
}
