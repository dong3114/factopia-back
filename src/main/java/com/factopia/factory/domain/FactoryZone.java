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

    private double fzXStart; // x_start
    private double fzYStart; // y_start
    private double fzZStart; // z_start
    private double fzXEnd;   // x_end
    private double fzYEnd;   // y_end
    private double fzZEnd;   // z_end

    private String zoneDescription; // zone_discription

    private Timestamp fzCreateTime;   // created_time
    private Timestamp fzUpdateTime;   // update_time

    private List<FactorySection> factorySections; // 공장 사용 구역 정보 (1:N)
}
