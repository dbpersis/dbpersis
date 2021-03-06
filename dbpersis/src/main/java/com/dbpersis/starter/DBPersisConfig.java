package com.dbpersis.starter;


import com.dbpersis.service.MyDataSource;
import com.dbpersis.service.QueryService;


public class DBPersisConfig {

  public static void ConfigDBPool(String driver, String url, String username, String password,
      int maxActive) {
    try {
      MyDataSource.register(driver, url, username, password, maxActive);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void ConfigQueryBase(String queryYmlPath, String associationsYmlPath) {
	System.out.println("查询数据格式初始化！");
    QueryService.init(queryYmlPath, associationsYmlPath);
  }
}
