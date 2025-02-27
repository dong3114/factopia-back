package com.factopia.factory.domain;

import lombok.*;

import java.util.List;

/**
 *  공장관련 테이블의 join된 결과를 반환하기 위한 도메인
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FactoryDataResponse {
    private List<FactorySite> factorySites;
    private List<byte[]> thumbnails;
}
