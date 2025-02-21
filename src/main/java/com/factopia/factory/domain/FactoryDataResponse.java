package com.factopia.factory.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 *  공장관련 테이블의 join된 결과를 반환하기 위한 도메인
 */
@Data
@Builder
public class FactoryDataResponse {
    private FactorySite factorySite;
    private List<FactoryZone> factoryZones;
    private List<FactorySection> factorySections;
    private List<Object3D> object3DS;
    private byte[] thumbnail;
}
