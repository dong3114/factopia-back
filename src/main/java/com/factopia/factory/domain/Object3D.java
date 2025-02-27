package com.factopia.factory.domain;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Object3D {
    private String objectNo;   // object_no (PK)
    private String factorySectionNo;  // f_section_no (FK)

    private Double xPosition; // x_pos
    private Double yPosition; // y_pos
    private Double zPosition; // z_pos

    private Double xSize; // x_size
    private Double ySize; // y_size
    private Double zSize; // z_size

    private String color; // color
    private String classification; // classification
    private String geometryData; // geometry
    private String materialData; // material

    private Double rotationX; // rotation_x
    private Double rotationY; // rotation_y
    private Double rotationZ; // rotation_z

    private Timestamp oCreateTime; // create_time
    private Timestamp oUpdateTime; // update_time
}
