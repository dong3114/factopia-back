package com.factopia.factory.mapper;

import com.factopia.factory.domain.FactoryDataResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FactoryMapper {
    FactoryDataResponse getFactoryAllData(@Param("factoryNo")String factoryNo);
    List<String> getAllFactoryNo(@Param("enterpriseNo")String enterpriseNo);
}
