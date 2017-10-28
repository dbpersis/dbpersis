package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.dbpersis.service.MyDataSource;
import com.terry.starter.MyDataSourceTransactionManager;

@Configuration
public class DatabaseConfiguration {
	@Bean
    public PlatformTransactionManager transactionManager(MyDataSource dataSource) {
        return new MyDataSourceTransactionManager(dataSource);
    }
}
