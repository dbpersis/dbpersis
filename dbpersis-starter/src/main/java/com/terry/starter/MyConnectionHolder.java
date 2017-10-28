package com.terry.starter;

import java.sql.Connection;

import org.springframework.jdbc.datasource.ConnectionHolder;

public class MyConnectionHolder extends ConnectionHolder {

	public MyConnectionHolder(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void setTransactionActive(boolean transactionActive) {
		super.setTransactionActive(transactionActive);
	}
	
	public void setTansActive(boolean transactionActive){
		this.setTransactionActive(transactionActive);
	}
}
