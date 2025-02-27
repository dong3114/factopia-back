package com.factopia.factory.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FactorySection {
    private String factorySectionNo; // f_section_no (PK)
    private String factoryZoneNo;    // f_zone_no (FK)

    private double xStart; // x_start
    private double yStart; // y_start
    private double zStart; // z_start
    private double xEnd;   // x_end
    private double yEnd;   // y_end
    private double zEnd;   // z_end

    private String usedDescription; // used_discription

    private Timestamp createTime;   // created_time
    private Timestamp updateTime;   // update_time

    private List<Object3D> object3DS; // 3D 오브젝트 정보 (1:N)
}