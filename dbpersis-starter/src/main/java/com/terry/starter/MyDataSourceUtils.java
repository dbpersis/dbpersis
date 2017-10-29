package com.terry.starter;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
<<<<<<< HEAD
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.dbpersis.service.MyDataSource;

public abstract class MyDataSourceUtils extends DataSourceUtils{
	
	public static Connection getConnection(MyDataSource dataSource) throws CannotGetJdbcConnectionException {
		try {
			return doGetConnection(dataSource);
		}
		catch (SQLException ex) {
			throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
		}
	}
	public static Connection doGetConnection(MyDataSource dataSource) throws SQLException {
		Assert.notNull(dataSource, "No DataSource specified");

		MyConnectionHolder conHolder = (MyConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
		if (conHolder != null && (conHolder.hasConnection() || conHolder.isSynchronizedWithTransaction())) {
			conHolder.requested();
			//if (!conHolder.hasConnection()) {
			//	logger.debug("Fetching resumed JDBC Connection from DataSource");
			//	conHolder.setConnection(dataSource.getConnection());
			//}
			return conHolder.getConnection();
		}
		// Else we either got no holder or an empty thread-bound holder here.

		//logger.debug("Fetching JDBC Connection from DataSource");
		Connection con = dataSource.getConnection();

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			//logger.debug("Registering transaction synchronization for JDBC Connection");
			// Use same Connection for further JDBC actions within the transaction.
			// Thread-bound object will get removed by synchronization at transaction completion.
			MyConnectionHolder holderToUse = conHolder;
			if (holderToUse == null) {
				holderToUse = new MyConnectionHolder(con);
			}
			else {
				holderToUse.setConnection(con);
			}
			holderToUse.requested();
			TransactionSynchronizationManager.registerSynchronization(
					new ConnectionSynchronization(holderToUse, dataSource));
			holderToUse.setSynchronizedWithTransaction(true);
			if (holderToUse != conHolder) {
				TransactionSynchronizationManager.bindResource(dataSource, holderToUse);
			}
		}

		return con;
	}
	private static int getConnectionSynchronizationOrder(DataSource dataSource) {
		int order = CONNECTION_SYNCHRONIZATION_ORDER;
		DataSource currDs = dataSource;
		while (currDs instanceof DelegatingDataSource) {
			order--;
			currDs = ((DelegatingDataSource) currDs).getTargetDataSource();
		}
		return order;
	}
	private static class ConnectionSynchronization extends TransactionSynchronizationAdapter {

		private final MyConnectionHolder connectionHolder;

		private final DataSource dataSource;

		private int order;

		private boolean holderActive = true;

		public ConnectionSynchronization(MyConnectionHolder connectionHolder, DataSource dataSource) {
			this.connectionHolder = connectionHolder;
			this.dataSource = dataSource;
			this.order = getConnectionSynchronizationOrder(dataSource);
		}

		@Override
		public int getOrder() {
			return this.order;
		}

		@Override
		public void suspend() {
			if (this.holderActive) {
				TransactionSynchronizationManager.unbindResource(this.dataSource);
				if (this.connectionHolder.hasConnection() && !this.connectionHolder.isOpen()) {
					// Release Connection on suspend if the application doesn't keep
					// a handle to it anymore. We will fetch a fresh Connection if the
					// application accesses the ConnectionHolder again after resume,
					// assuming that it will participate in the same transaction.
					releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
					this.connectionHolder.setConnection(null);
				}
			}
		}

		@Override
		public void resume() {
			if (this.holderActive) {
				TransactionSynchronizationManager.bindResource(this.dataSource, this.connectionHolder);
			}
		}

		@Override
		public void beforeCompletion() {
			if (!this.connectionHolder.isOpen()) {
				TransactionSynchronizationManager.unbindResource(this.dataSource);
				this.holderActive = false;
				if (this.connectionHolder.hasConnection()) {
					releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
				}
			}
		}

		@Override
		public void afterCompletion(int status) {
			// If we haven't closed the Connection in beforeCompletion,
			// close it now. The holder might have been used for other
			// cleanup in the meantime, for example by a Hibernate Session.
			if (this.holderActive) {
				// The thread-bound ConnectionHolder might not be available anymore,
				// since afterCompletion might get called from a different thread.
				TransactionSynchronizationManager.unbindResourceIfPossible(this.dataSource);
				this.holderActive = false;
				if (this.connectionHolder.hasConnection()) {
					releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
					// Reset the ConnectionHolder: It might remain bound to the thread.
					this.connectionHolder.setConnection(null);
				}
			}
			this.connectionHolder.reset();
		}
	}

=======
import org.springframework.transaction.TransactionDefinition;

public class MyDataSourceUtils extends DataSourceUtils {

  public static void releaseConnection(Connection con, MyDataSource dataSource) {
    dataSource.release(con);
  }

  public static Integer prepareConnectionForTransaction(Connection con,
      TransactionDefinition definition) throws SQLException {
    if (definition != null && definition.isReadOnly()) {
      try {
        con.setReadOnly(true);
      } catch (SQLException ex) {
        Throwable exToCheck = ex;
        while (exToCheck != null) {
          if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
            // Assume it's a connection timeout that would otherwise get lost: e.g. from JDBC 4.0
            throw ex;
          }
          exToCheck = exToCheck.getCause();
        }
      } catch (RuntimeException ex) {
        Throwable exToCheck = ex;
        while (exToCheck != null) {
          if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
            // Assume it's a connection timeout that would otherwise get lost: e.g. from Hibernate
            throw ex;
          }
          exToCheck = exToCheck.getCause();
        }
      }
    }

    // Apply specific isolation level, if any.
    Integer previousIsolationLevel = null;
    if (definition != null
        && definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {

      int currentIsolation = con.getTransactionIsolation();
      if (currentIsolation != definition.getIsolationLevel()) {
        previousIsolationLevel = currentIsolation;
        con.setTransactionIsolation(definition.getIsolationLevel());
      }
    }
    return previousIsolationLevel;
  }
>>>>>>> d70680c3286d30f5dc4ffb86b7f0ee27d967f612
}
