package com.terry.starter;

import com.dbpersis.service.MyDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SuppressWarnings("serial")
public  class MyDataSourceTransactionManager extends DataSourceTransactionManager {
	private MyDataSource dataSource;
	public MyDataSourceTransactionManager() {
		super();
	}
	
	public MyDataSourceTransactionManager(MyDataSource dataSource){
		super(dataSource);		
		this.dataSource=dataSource;
	}
	
	@Override
	protected Object doGetTransaction() {
		MyDataSourceTransactionObject txObject = new MyDataSourceTransactionObject();
		txObject.setSavepointAllowed(isNestedTransactionAllowed());
		MyConnectionHolder conHolder =
				(MyConnectionHolder) TransactionSynchronizationManager.getResource(this.dataSource);
		txObject.setConnectionHolder(conHolder, false);
		return txObject;
	}
	@Override
	protected boolean isExistingTransaction(Object transaction) {
		MyDataSourceTransactionObject txObject = (MyDataSourceTransactionObject) transaction;
		return (txObject.getConnectionHolder() != null && txObject.getConnectionHolder().isTransactionActive());
	}
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		MyDataSourceTransactionObject txObject = (MyDataSourceTransactionObject) transaction;
		Connection con = null;

		try {
			if (txObject.getConnectionHolder() == null ||
					txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
				Connection newCon = this.dataSource.getConnection();
				//if (logger.isDebugEnabled()) {
				//	logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
				//}
				txObject.setConnectionHolder(new MyConnectionHolder(newCon), true);
			}

			txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
			con = txObject.getConnectionHolder().getConnection();

			Integer previousIsolationLevel = MyDataSourceUtils.prepareConnectionForTransaction(con, definition);
			txObject.setPreviousIsolationLevel(previousIsolationLevel);

			// Switch to manual commit if necessary. This is very expensive in some JDBC drivers,
			// so we don't want to do it unnecessarily (for example if we've explicitly
			// configured the connection pool to set it already).
			if (con.getAutoCommit()) {
				txObject.setMustRestoreAutoCommit(true);
				//if (logger.isDebugEnabled()) {
				//	logger.debug("Switching JDBC Connection [" + con + "] to manual commit");
				//}
				con.setAutoCommit(false);
			}
			txObject.getConnectionHolder().setTransactionActive(true);

			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
			}

			// Bind the session holder to the thread.
			if (txObject.isNewConnectionHolder()) {
				TransactionSynchronizationManager.bindResource(getDataSource(), txObject.getConnectionHolder());
			}
		}

		catch (Throwable ex) {
			if (txObject.isNewConnectionHolder()) {
				DataSourceUtils.releaseConnection(con, this.dataSource);
				txObject.setConnectionHolder(null, false);
			}
			throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", ex);
		}
	}
	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		MyDataSourceTransactionObject txObject = (MyDataSourceTransactionObject) status.getTransaction();
		Connection con = txObject.getConnectionHolder().getConnection();
		//if (status.isDebug()) {
		//	logger.debug("Committing JDBC transaction on Connection [" + con + "]");
		//}
		try {
			con.commit();
		}
		catch (SQLException ex) {
			throw new TransactionSystemException("Could not commit JDBC transaction", ex);
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		MyDataSourceTransactionObject txObject = (MyDataSourceTransactionObject) status.getTransaction();
		Connection con = txObject.getConnectionHolder().getConnection();
		//if (status.isDebug()) {
		//	logger.debug("Rolling back JDBC transaction on Connection [" + con + "]");
		//}
		try {
			con.rollback();
		}
		catch (SQLException ex) {
			throw new TransactionSystemException("Could not roll back JDBC transaction", ex);
		}
	}

	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		MyDataSourceTransactionObject txObject = (MyDataSourceTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			//logger.debug("Setting JDBC transaction [" + txObject.getConnectionHolder().getConnection() +
			//		"] rollback-only");
		}
		txObject.setRollbackOnly();
		super.setDataSource(dataSource);
	}
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		MyDataSourceTransactionObject txObject = (MyDataSourceTransactionObject) transaction;

		// Remove the connection holder from the thread, if exposed.
		if (txObject.isNewConnectionHolder()) {
			TransactionSynchronizationManager.unbindResource(this.dataSource);
		}

		// Reset connection.
		Connection con = txObject.getConnectionHolder().getConnection();
		try {
			if (txObject.isMustRestoreAutoCommit()) {
				con.setAutoCommit(true);
			}
			MyDataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
		}
		catch (Throwable ex) {
			//logger.debug("Could not reset JDBC Connection after transaction", ex);
		}

		if (txObject.isNewConnectionHolder()) {
			//if (logger.isDebugEnabled()) {
			//	logger.debug("Releasing JDBC Connection [" + con + "] after transaction");
			//}
			MyDataSourceUtils.releaseConnection(con, this.dataSource);
		}

		txObject.getConnectionHolder().clear();
	}
	
	/**
	 * DataSource transaction object, representing a ConnectionHolder.
	 * Used as transaction object by DataSourceTransactionManager.
	 */
	private static class MyDataSourceTransactionObject extends JdbcTransactionObjectSupport {

		private boolean newConnectionHolder;

		private boolean mustRestoreAutoCommit;

		public void setConnectionHolder(MyConnectionHolder connectionHolder, boolean newConnectionHolder) {
			super.setConnectionHolder(connectionHolder);
			this.newConnectionHolder = newConnectionHolder;
		}

		public boolean isNewConnectionHolder() {
			return this.newConnectionHolder;
		}

		public void setMustRestoreAutoCommit(boolean mustRestoreAutoCommit) {
			this.mustRestoreAutoCommit = mustRestoreAutoCommit;
		}

		public boolean isMustRestoreAutoCommit() {
			return this.mustRestoreAutoCommit;
		}

		public void setRollbackOnly() {
			getConnectionHolder().setRollbackOnly();
		}

		@Override
		public boolean isRollbackOnly() {
			return getConnectionHolder().isRollbackOnly();
		}
		@Override
		public MyConnectionHolder getConnectionHolder() {
			return (MyConnectionHolder) super.getConnectionHolder();
		}
	}

}
