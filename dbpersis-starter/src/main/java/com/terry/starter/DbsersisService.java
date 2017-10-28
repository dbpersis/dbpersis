package com.terry.starter;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ResourceUtils;

import com.dbpersis.service.PojoDataset;
import com.dbpersis.service.QueryService;
import com.dbpersis.starter.DBPersisConfig;
import com.dbpersis.utils.BeanHandler;

@ConfigurationProperties("com.terry.starter.service")
public class DbsersisService {
	private String driver;
	private String url;
	private String username;
	private String password;
	private String maxActive;	

    public DbsersisService(String driver, String url, String username, String password, String maxActive) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxActive = maxActive;
        
		try {
			DBPersisConfig.ConfigDBPool(driver, url, username, password, maxActive);
			DBPersisConfig.ConfigQueryBase(ResourceUtils.getFile("classpath:query").getPath(), ResourceUtils.getFile("classpath:association").getPath());
			ResourceUtils.getFile("classpath:query").getPath();
			ResourceUtils.getFile("classpath:association").getPath();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    public <T> List<T> queryService(String sqlName, BeanHandler<T> beanListHandler, Map<String, Object> params, int pageIndex,int pageSize) throws Exception{
    	return new QueryService().query(sqlName, beanListHandler, params,pageIndex,pageSize);
    }
    public Serializable save(Object object) throws Exception{
    	return new PojoDataset().save(object);
    }
    public Serializable update(Object object, List<String> param) throws Exception{
    	return new PojoDataset().update(object,param); 
    }
}
