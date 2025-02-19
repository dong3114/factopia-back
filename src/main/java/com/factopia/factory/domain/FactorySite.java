package com.factopia.factory.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class FactorySite {
    // 공장부지
    private String factoryNo;
    private String enterpriseNo;
    private String totalWidth; // 공장전체 'x축'
    private String totalHeight; // 공장전체 'y축'
    private String totalDepth; // 공장전체 'z축'
    private Date createTime;
    private Date updateTime;
    private String factorySiteName; // 공장부지 이름
}
