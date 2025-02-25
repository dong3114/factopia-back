package com.factopia.factory.service;

import com.factopia.factory.domain.FactoryDataResponse;
import com.factopia.factory.domain.FactorySite;
import com.factopia.factory.domain.Object3D;
import com.factopia.factory.mapper.FactoryMapper;
import com.factopia.factory.thumbnail.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public List<FactoryDataResponse> factoryAllData(List<String> factoryNos){
        if (factoryNos.isEmpty()){
            return List.of();
        }
        // 1️⃣ DB에서 데이터 조회
        List<FactoryDataResponse> factories = factoryMapper.getFactoryAllData(factoryNos);
        // 2️⃣ 썸네일 생성(병렬 스트림 사용)
        factories.parallelStream().forEach(factory -> {
            FactorySite factorySite = factory.getFactorySite();
            List<Object3D> object3DS =factory.getObject3DS() != null ? factory.getObject3DS() : new ArrayList<>();
            factory.setThumbnail(ThumbnailUtil.generateThumbnail(factorySite, object3DS));
        });
        return factories;
    }
}
