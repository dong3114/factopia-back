package com.factopia.factory.service;

import com.factopia.factory.domain.FactoryDataResponse;

import java.util.List;

public interface FactoryService {
    FactoryDataResponse factoryAllData(String factoryNo);
    List<String> getAllFactoryNo(String enterpriseNo);
}
