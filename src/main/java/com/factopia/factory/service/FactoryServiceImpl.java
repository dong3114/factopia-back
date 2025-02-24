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
    public FactoryDataResponse factoryAllData(String factoryNo){
        // 1️⃣ DB에서 데이터 조회
        FactoryDataResponse response = factoryMapper.getFactoryAllData(factoryNo);
        // 2️⃣ 데이터가 없을 경우, 기본값 설정
        if(response == null){
            response = FactoryDataResponse.builder()
                    .factorySite(FactorySite.builder().factoryNo(factoryNo).build()) // 기본 값 설정
                    .factoryZones(new ArrayList<>())
                    .factorySections(new ArrayList<>())
                    .object3DS(new ArrayList<>())
                    .thumbnail(new byte[0])
                    .build();
        }
        FactorySite factorySite = response.getFactorySite();
        // 3️⃣ object3DS 리스트 가져오기
        List<Object3D> object3DS = response.getObject3DS();
        if(object3DS == null){
            object3DS = new ArrayList<>();
            response.setObject3DS(object3DS);
        }
        // 4️⃣ 썸네일 생성
        byte[] thumbnail = ThumbnailUtil.generateThumbnail(factorySite, object3DS);
        response.setThumbnail(thumbnail);
        return response;
    }
}
