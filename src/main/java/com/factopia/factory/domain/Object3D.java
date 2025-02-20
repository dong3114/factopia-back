package com.factopia.factory.domain;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Object3D {
    private String objectNo; // 오브젝트 ID (PK)
    private String sectionNo; // 공장 사용구역 ID (FK)

    private Double xPosition; // X축 위치
    private Double yPosition; // Y축 위치
    private Double zPosition; // Z축 위치

    private Double xSize; // X축 크기
    private Double ySize; // Y축 크기
    private Double zSize; // Z축 크기

    private String color; // 색상

    @Builder.Default
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    private Timestamp updateTime = null;

    private String classification; // 분류
    private String geometry; // 기하학 데이터 (JSON)
    private String material; // 재질 정보 (JSON)

    private Double rotationX; // X축 회전
    private Double rotationY; // Y축 회전
    private Double rotationZ; // Z축 회전
}
