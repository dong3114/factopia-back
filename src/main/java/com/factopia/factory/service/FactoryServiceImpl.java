package com.factopia.factory.service;

import com.factopia.factory.domain.*;
import com.factopia.factory.mapper.FactoryMapper;
import com.factopia.factory.thumbnail.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FactoryServiceImpl implements FactoryService{
    @Autowired
    private final FactoryMapper factoryMapper;

    public List<String> getAllFactoryNo(String enterpriseNo){
        List<String> factorise = factoryMapper.getAllFactoryNo(enterpriseNo);
        if(factorise.isEmpty()){
            System.out.println("기업내부에 공장이 없습니다.");
            return List.of(); // 빈 리스트 반환 null 방지
        }
        return factorise;
    }

    /**
     * 썸네일 포함 공장 객체 반환
     */
    @Transactional
    public List<FactoryDataResponse> factoryAllData(String enterpriseNo){
        // 1. 기업 코드를 통해 공장부지 로드
        List<FactorySite> factorySites = factoryMapper.factorySitesLoad(enterpriseNo);
        // null 방지용 빈객체 반환
        if (factorySites.isEmpty()){
            return List.of();
        }
        // 2. 병렬 처리하여 하위 계층 데이터 로드
        return factorySites.parallelStream().map(factorySite -> {
            String factoryNo = factorySite.getFactoryNo();
            List<FactoryZone> factoryZones = factoryMapper.factoryZonesLoad(factoryNo);
            // zone 리스트화
            factoryZones.forEach(zone -> {
                List<FactorySection> factorySections = factoryMapper.factorySectionsLoad(zone.getFactoryZoneNo());
                // section 리스트화
                factorySections.forEach(section -> {
                    // object 3D 데이터 로드
                    List<Object3D> object3Ds = factoryMapper.object3DsLoad(section.getFactorySectionNo());
                    section.setObject3DS(object3Ds);
                });
                zone.setFactorySections(factorySections);
            });
            factorySite.setFactoryZones(factoryZones);
            return FactoryDataResponse.builder()
                    .factorySite(factorySite)
                    .factoryZones(factoryZones)
                    .factorySections(factoryZones.stream()
                            .flatMap(zone -> zone.getFactorySections().stream())
                            .collect(Collectors.toList()))
                    .object3DS(extractObjects(factoryZones))
                    .thumbnail(ThumbnailUtil.generateThumbnail(factorySite, extractObjects(factoryZones)))
                    .build();
        }).collect(Collectors.toList());
    }

    // factoryZones를 순회하며 하위 계층의 object3DS만 추출
    private List<Object3D> extractObjects(List<FactoryZone> zones) {
        List<Object3D> objects = new ArrayList<>();
        for (FactoryZone zone : zones) {
            for (FactorySection section : zone.getFactorySections()) {
                objects.addAll(section.getObject3DS());
            }
        }
        return objects;
    }

}
