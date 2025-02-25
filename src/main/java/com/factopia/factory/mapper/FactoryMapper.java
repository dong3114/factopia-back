package com.factopia.factory.mapper;

import com.factopia.factory.domain.FactoryDataResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FactoryMapper {
    List<FactoryDataResponse> getFactoryAllData(@Param("factoryNo")List<String> factoryNos);
    List<String> getAllFactoryNo(@Param("enterpriseNo")String enterpriseNo);
}
