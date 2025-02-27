package com.factopia.factory.mapper;

import com.factopia.factory.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface FactoryMapper {
    // 기업 별 보유 공장코드 로드
    List<String> getAllFactoryNo(@Param("enterpriseNo") String enterpriseNo);

    // 공장 계층별 탐색
    List<FactorySite> factorySitesLoad(@Param("enterpriseNo") String enterpriseNo);
    List<FactoryZone> factoryZonesLoad(@Param("factoryNo") String factoryNo);
    List<FactorySection> factorySectionsLoad(@Param("factoryZoneNo") String factoryZoneNo);
    List<Object3D> object3DsLoad(@Param("factorySectionNo") String factorySectionNo);
}
