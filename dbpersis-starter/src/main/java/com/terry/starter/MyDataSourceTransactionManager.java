package com.terry.starter;

import com.dbpersis.service.MyDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@SuppressWarnings("serial")
public class MyDataSourceTransactionManager extends DataSourceTransactionManager {
	
	public MyDataSourceTransactionManager() {
		super();
	}
	
	public MyDataSourceTransactionManager(MyDataSource dataSource){
		this();
		super.setDataSource(dataSource);
		super.afterPropertiesSet();
	}
}
