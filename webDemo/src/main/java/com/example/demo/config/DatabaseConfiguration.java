package com.example.demo.config;

import com.dbpersis.service.MyDataSource;
import com.terry.starter.MyDataSourceTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DatabaseConfiguration {
  @Bean
  public PlatformTransactionManager transactionManager(MyDataSource dataSource) {
    return new MyDataSourceTransactionManager(dataSource);
  }
}
