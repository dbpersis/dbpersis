package com.example.demo;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;

import com.dbpersis.service.MyDataSource;
import com.dbpersis.starter.DBPersisConfig;
import com.terry.starter.MyDataSourceTransactionManager;

@SpringBootApplication
@EnableTransactionManagement
public class DemoWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebApplication.class, args);
	}
	@Bean
    public PlatformTransactionManager transactionManager(MyDataSource dataSource) {
        return new MyDataSourceTransactionManager(dataSource);
    }
}
