package com.factopia.factory.service;

import com.factopia.factory.domain.FactoryDataResponse;

import java.util.List;

public interface FactoryService {
    List<FactoryDataResponse> factoryAllData(List<String> factoryNos);
    List<String> getAllFactoryNo(String enterpriseNo);
}
