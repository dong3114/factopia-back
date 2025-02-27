package com.factopia.factory.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더사용강제
@Builder
public class FactorySite {
    private String factoryNo;       // f_no (PK)
    private String enterpriseNo;    // e_no (FK)

    private Double totalWidth;      // total_width
    private Double totalHeight;     // total_height
    private Double totalDepth;      // total_depth

    private Timestamp fCreateTime;   // created_time
    private Timestamp fUpdateTime;   // update_time

    private String factorySiteName; // factory_site_name

    private List<FactoryZone> factoryZones;  // 공장 구역 정보 (1:N)
}
