package com.dbpersis.service;

import java.sql.Connection;

/**
 * 辅助类,用于管理数据库连接.
 */
public class ConnectionHolder {

  private static final ThreadLocal<Connection> CONNECTION_HOLDER  = new ThreadLocal<>();

  public static Connection get() {
    return CONNECTION_HOLDER.get();
  }

  public static void set(Connection conn) {
    if (conn == null) {
      remove();
      return;
    }
    CONNECTION_HOLDER.set(conn);
  }

  public static void remove() {
    CONNECTION_HOLDER.remove();
  }
}
