package com.dbpersis.starter;


import com.dbpersis.service.MyDataSource;
import com.dbpersis.service.QueryService;


public class DBPersisConfig {
	
	public static void ConfigDBPool(String driver,String url,String username,String password,String maxActive){
		try {
			MyDataSource.register(driver, url, username, password, maxActive);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ConfigQueryBase(String queryYmlPath,String associationsYmlPath){
		QueryService.init(queryYmlPath, associationsYmlPath);
	}
}
