package com.dbpersis.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import java.util.logging.Logger;
import javax.sql.DataSource;
import com.dbpersis.utils.BeanHandler;

public class MyDataSource implements DataSource {
	public static LinkedList<Connection> connpool = new LinkedList<Connection>();

	public static void register(String driver, String url, String username, String password, int maxActive)
			throws Exception {
		if (connpool.size() == 0) {
			Integer DATABASE_CONNECTION_POOL_SIZE = Integer.valueOf(maxActive);
			// 预先 添加驱动信息
			Class.forName(driver);
			for (int index = 0; index < DATABASE_CONNECTION_POOL_SIZE; index++) {
				Connection conn = DriverManager.getConnection(url, username, password);
				connpool.add(conn);
			}

		}
	}

	public static void release(Connection conn) {
		try {
			if (conn != null) {
				connpool.add(conn);
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库连接对象时出错！ :\n" + e);
		}
	}

	/**
	 * 释放数据库连接对象以及数据库查询对象
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param stmt
	 *            数据库查询语句
	 */
	public static void release(Connection conn, Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库查询对象Statement时出错！ :\n" + e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		release(conn);
	}

	public static void release(Connection conn, Statement stmt, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(" 关闭数据库结果集对象时出错！:\n" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			throw new RuntimeException("释放数据库查询对象Statement时出错！ :\n" + e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		release(conn);
	}

	/**
	 * 从数据库连接池中获取数据库连接对象，同时维护好池的内容实时更新
	 */
	@Override
	public Connection getConnection() throws SQLException {
		if (connpool.size() <= 0) {
			throw new RuntimeException("数据库忙，请待会再试试吧！");
		}
		// 需要注意的是，不能使用get方式（这个方法知识返回一个引用而已）,
		// 应该在获取的同时，删除掉这个链接，之后再还回来.现在注意是返还给数据库连接池！！！
		Connection conn = connpool.removeFirst();
		MyConnection myconn = new MyConnection(conn);

		// 从这里开始返回的就是一个数据库连接池对象的conn
		return myconn;
	}

	public <T> List<T> query(String sql, BeanHandler<T> beanListHandler) throws Exception {
		Connection conn = this.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		// logger.error(sql);
		ResultSet rs = ps.executeQuery();
		List<T> result = beanListHandler.handle(rs);
		MyDataSource.release(conn, ps);
		return result;
	}

	public <T> void update(Object object, List<String> param) throws Exception {
		String sql = "";
		Class<? extends Object> c = object.getClass();

		Field[] fields = c.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append(" update ");
		sb.append(c.getSimpleName());
		sb.append(" set ");

		for (Field f : fields) {
			f.setAccessible(true);
			if (verifyFiled(f)) {
				sb.append(f.getName() + "='" + f.get(object) + "'");
				sb.append(",");
			}

		}
		sql = sb.substring(0, sb.length() - 1).toString();
		StringBuffer where = new StringBuffer();
		where.append("where 1=1 ");
		for (int i = 0; i < param.size(); i++) {
			where.append(" and ");
			where.append(param.get(i));
		}
		sql += where.toString();
		// logger.error(sql);
		Connection conn = this.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
		MyDataSource.release(conn, ps);

	}

	public <T> void save(Object object) throws Exception {
		Class<? extends Object> c = object.getClass();
		String sql = "";
		Field[] fields = c.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(c.getSimpleName());
		sb.append("(");
		StringBuffer value = new StringBuffer();
		value.append(" values (");
		for (Field f : fields) {
			if (verifyFiled(f)) {
				sb.append(f.getName());
				sb.append(",");
			}
		}
		for (Field f : fields) {
			if (verifyFiled(f)) {
				f.setAccessible(true);
				value.append("'" + f.get(object) + "'");
				value.append(",");
			}
		}
		sql = sb.substring(0, sb.length() - 1).toString();
		sql += ")";
		sql += value.substring(0, value.length() - 1).toString();
		sql += ")";
		// logger.error(sql);
		Connection conn = this.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
		MyDataSource.release(conn, ps);
	}

	private boolean verifyFiled(Field f) {
		if (f.getType().getSimpleName().equals("String")) {
			return true;
		}
		if (f.getType().getSimpleName().equals("int")) {
			return true;
		}
		if (f.getType().getSimpleName().equals("long")) {
			return true;
		}

		if (f.getType().getSimpleName().equals("Date")) {
			return true;
		}
		return false;
	}
	///////////////////////////////////////////////////////////////////////// datasource接口的实现方法开始

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	///////////////////////////////////////////////////////////////////////// datasource接口的实现方法结束

	/**
	 * 包装设计模式实现流程： 1.创建一个类，实现与被增强对象相同的接口 2.将被增强对象当做自定义类的一个成员变量
	 * 3.定义一个构造方法，将被增强对象传递进去 4.增强想要增强的方法，进行覆盖即可 5.对于不像被增强的方法，调用被增强对象的方法进行处理即可
	 * 
	 * @author 郭璞
	 *
	 */

	
	class MyConnection implements Connection {

		private Connection conn;

		public MyConnection(Connection conn) {
			this.conn = conn;
		}

		/**
		 * 自定义的包装设计模式类，增强close方法， 将数据库链接资源返还给数据库连接池，而不是数据库
		 */
		public void close() {
			connpool.add(conn);
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.unwrap(iface);
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.isWrapperFor(iface);
		}

		@Override
		public Statement createStatement() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createStatement();
		}

		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareCall(sql);
		}

		@Override
		public String nativeSQL(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.nativeSQL(sql);
		}

		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setAutoCommit(autoCommit);
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getAutoCommit();
		}

		@Override
		public void commit() throws SQLException {
			// TODO Auto-generated method stub
			this.conn.commit();
		}

		@Override
		public void rollback() throws SQLException {
			// TODO Auto-generated method stub
			this.conn.rollback();
		}

		@Override
		public boolean isClosed() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.isClosed();
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getMetaData();
		}

		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setReadOnly(readOnly);
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.isReadOnly();
		}

		@Override
		public void setCatalog(String catalog) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setCatalog(catalog);
		}

		@Override
		public String getCatalog() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getCatalog();
		}

		@Override
		public void setTransactionIsolation(int level) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setTransactionIsolation(level);
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getTransactionIsolation();
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getWarnings();
		}

		@Override
		public void clearWarnings() throws SQLException {
			// TODO Auto-generated method stub
			this.conn.clearWarnings();
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createStatement(resultSetType, resultSetConcurrency);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getTypeMap();
		}

		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setTypeMap(map);
		}

		@Override
		public void setHoldability(int holdability) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setHoldability(holdability);
		}

		@Override
		public int getHoldability() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getHoldability();
		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.setSavepoint();
		}

		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.setSavepoint(name);
		}

		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.rollback(savepoint);
		}

		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.releaseSavepoint(savepoint);
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql, autoGeneratedKeys);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql, columnIndexes);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.prepareStatement(sql, columnNames);
		}

		@Override
		public Clob createClob() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createClob();
		}

		@Override
		public Blob createBlob() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createBlob();
		}

		@Override
		public NClob createNClob() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createNClob();
		}

		@Override
		public SQLXML createSQLXML() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createSQLXML();
		}

		@Override
		public boolean isValid(int timeout) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.isValid(timeout);
		}

		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			// TODO Auto-generated method stub
			this.conn.setClientInfo(name, value);
		}

		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			// TODO Auto-generated method stub
			this.conn.setClientInfo(properties);
		}

		@Override
		public String getClientInfo(String name) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getClientInfo(name);
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getClientInfo();
		}

		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createArrayOf(typeName, elements);
		}

		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.createStruct(typeName, attributes);
		}

		@Override
		public void setSchema(String schema) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setSchema(schema);
		}

		@Override
		public String getSchema() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getSchema();
		}

		@Override
		public void abort(Executor executor) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.abort(executor);
		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
			// TODO Auto-generated method stub
			this.conn.setNetworkTimeout(executor, milliseconds);
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return this.conn.getNetworkTimeout();
		}

	}
	///////////////////////////////////////////////////////////////////////////// 包装设计模式结束
}
