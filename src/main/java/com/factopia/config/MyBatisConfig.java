package com.factopia.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.factopia")
public class MyBatisConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResource("classpath:/mapper/*.xml")
        );
        factoryBean.setTypeAliasesPackage("com.factopia");
        return factoryBean.getObject();
    }

    // 트랜잭션 처리
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public org.apache.ibatis.session.Configuration myBatisGlobalConfig() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 언더스코어 → 카멜케이스 자동 변환
        configuration.setCacheEnabled(true);            // 2차 캐시 활성화
        configuration.setLazyLoadingEnabled(false);     // 지연 로딩 비활성화
        configuration.setDefaultFetchSize(100);         // 기본 Fetch 크기
        configuration.setDefaultStatementTimeout(30);   // 기본 SQL 시간 초과 설정 (초 단위)
        return configuration;
    }

}
