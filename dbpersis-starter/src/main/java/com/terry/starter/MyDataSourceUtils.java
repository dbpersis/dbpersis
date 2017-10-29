package com.terry.starter;

import com.dbpersis.service.MyDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.datasource.DataSourceUtils;
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
}
