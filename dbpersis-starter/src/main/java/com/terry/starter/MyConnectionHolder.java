package com.terry.starter;

import java.sql.Connection;
import org.springframework.jdbc.datasource.ConnectionHandle;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.SimpleConnectionHandle;

public class MyConnectionHolder extends ConnectionHolder {
	public MyConnectionHolder(ConnectionHandle connectionHandle) {
		super(connectionHandle);
	}

	
	public MyConnectionHolder(Connection connection) {
		super(connection);
	}

	/**
	 * Create a new ConnectionHolder for the given JDBC Connection,
	 * wrapping it with a {@link SimpleConnectionHandle}.
	 * @param connection the JDBC Connection to hold
	 * @param transactionActive whether the given Connection is involved
	 * in an ongoing transaction
	 * @see SimpleConnectionHandle
	 */
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
}
