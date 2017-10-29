package com.terry.starter;

import java.sql.Connection;
import org.springframework.jdbc.datasource.ConnectionHandle;
import org.springframework.jdbc.datasource.ConnectionHolder;

public class MyConnectionHolder extends ConnectionHolder {
	public MyConnectionHolder(ConnectionHandle connectionHandle) {
		super(connectionHandle);
	}

	
	public MyConnectionHolder(Connection connection) {
		super(connection);
	}
	
	public MyConnectionHolder(Connection connection, boolean transactionActive) {
		super(connection,transactionActive);
	}
	@Override
	public void setTransactionActive(boolean transactionActive) {
		//this.transactionActive = transactionActive;
		super.setTransactionActive(transactionActive);
	}
	@Override
	public boolean hasConnection() {
		return super.hasConnection();
	}

	public void setConnection(Connection connection) {
		super.setConnection(connection);
	}
	
	public boolean isTransactionActive() {
		return this.isTransactionActive();
	}

}
