package com.terry.starter;

import java.sql.Connection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.dbpersis.service.MyDataSource;

@SuppressWarnings("serial")
public class MyDataSourceTransactionManager extends DataSourceTransactionManager {
	
	public MyDataSourceTransactionManager() {
		//setNestedTransactionAllowed(true);
		super();
	}
	
	public MyDataSourceTransactionManager(MyDataSource dataSource){
		this();
		super.setDataSource(dataSource);
		super.afterPropertiesSet();
	}

	
	
}
