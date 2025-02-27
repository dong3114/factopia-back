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
    @Transactional(readOnly = true)
    public FactoryDataResponse factoryAllData(String enterpriseNo) {
        List<FactorySite> factorySites = factoryMapper.getFactoryAllData(enterpriseNo);
        // 썸네일 객체 반환
        List<byte[]> thumbnails = factorySites.stream()
                .map(factorySite -> ThumbnailUtil.generateThumbnail(factorySite, extractObjects(factorySite.getFactoryZones())))
                .collect(Collectors.toList());

        return FactoryDataResponse.builder()
                .factorySites(factorySites)
                .thumbnails(thumbnails)
                .build();
    }

    /**
     * 특정 공장에 속한 모든 `FactorySection` 목록을 추출하는 메서드
     */
    private List<FactorySection> extractSections(List<FactoryZone> factoryZones) {
        if (factoryZones == null) return List.of();
        return factoryZones.stream()
                .flatMap(zone -> zone.getFactorySections().stream())
                .collect(Collectors.toList());
    }

    /**
     * 특정 공장에 속한 모든 `Object3D` 목록을 추출하는 메서드
     */
    private List<Object3D> extractObjects(List<FactoryZone> factoryZones) {
        if (factoryZones == null) return List.of();
        return factoryZones.stream()
                .flatMap(zone -> zone.getFactorySections().stream())
                .flatMap(section -> section.getObject3DS().stream())
                .collect(Collectors.toList());
    }


}
