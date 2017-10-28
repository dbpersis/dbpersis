package com.terry.starter;

import java.sql.Connection;

import org.springframework.beans.factory.InitializingBean;
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
public class MyDataSourceTransactionManager extends AbstractPlatformTransactionManager
implements ResourceTransactionManager, InitializingBean {
	private MyDataSource dataSource;
	
	public MyDataSourceTransactionManager() {
		setNestedTransactionAllowed(true);
	}
	public MyDataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(MyDataSource dataSource) {
		this.dataSource = dataSource;
	}
	public MyDataSourceTransactionManager(MyDataSource dataSource){
		this();
		setDataSource(dataSource);
		afterPropertiesSet();
	}

	@Override
	protected void doCommit(DefaultTransactionStatus arg0) throws TransactionException {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void doRollback(DefaultTransactionStatus arg0) throws TransactionException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterPropertiesSet() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getResourceFactory() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void doBegin(Object arg0, TransactionDefinition arg1) throws TransactionException {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected Object doGetTransaction() throws TransactionException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
