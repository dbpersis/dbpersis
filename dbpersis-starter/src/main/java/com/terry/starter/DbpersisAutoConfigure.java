package com.terry.starter;

import com.dbpersis.service.MyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnClass(DbsersisService.class)
@EnableConfigurationProperties(DbpersisServiceProperties.class)
public class DbpersisAutoConfigure {

  @Autowired
  private DbpersisServiceProperties properties;

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "com.terry.starter", value = "enabled", havingValue = "true")
  DbsersisService exampleService() {
	  
    return new DbsersisService(properties.getDriver(), properties.getUrl(),
        properties.getUsername(), properties.getPassword(), properties.getMaxActive(),
        dataSource());
  }

  @Bean
  @ConditionalOnMissingBean(PlatformTransactionManager.class)
  @ConditionalOnBean(MyDataSource.class)
  public MyDataSourceTransactionManager transactionManager() {
    return new MyDataSourceTransactionManager(dataSource());
  }

  @Bean
  public MyDataSource dataSource() {
    return new MyDataSource();
  }
}
