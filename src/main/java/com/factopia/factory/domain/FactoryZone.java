package com.factopia.factory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactoryZone {
    private String factoryZoneNo; // 공장구역 ID (PK)
    private String factoryNo; // 공장부지 ID (FK)

    private double xStart; // X축 시작 좌표
    private double yStart; // Y축 시작 좌표
    private double zStart; // Z축 시작 좌표

    private double xEnd; // X축 끝 좌표
    private double yEnd; // Y축 끝 좌표
    private double zEnd; // Z축 끝 좌표

    private String zoneDescription; // 구역 설명

    @Builder.Default
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    private Timestamp updateTime = null;
}
