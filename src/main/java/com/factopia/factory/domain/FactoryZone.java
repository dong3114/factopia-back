package com.factopia.factory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactoryZone {
    private String factoryZoneNo; // f_zone_no (PK)
    private String factoryNo;     // f_no (FK)

    private double xStart; // x_start
    private double yStart; // y_start
    private double zStart; // z_start
    private double xEnd;   // x_end
    private double yEnd;   // y_end
    private double zEnd;   // z_end

    private String zoneDescription; // zone_discription

    private Timestamp createTime;   // created_time
    private Timestamp updateTime;   // update_time

    private List<FactorySection> factorySections; // 공장 사용 구역 정보 (1:N)
}
