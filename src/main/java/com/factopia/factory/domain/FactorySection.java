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

    private double fsXStart; // x_start
    private double fsYStart; // y_start
    private double fsZStart; // z_start
    private double fsXEnd;   // x_end
    private double fsYEnd;   // y_end
    private double fsZEnd;   // z_end

    private String usedDescription; // used_discription

    private Timestamp fsCreateTime;   // created_time
    private Timestamp fsUpdateTime;   // update_time

    private List<Object3D> object3DS; // 3D 오브젝트 정보 (1:N)
}