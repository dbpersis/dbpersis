package com.terry.starter;

import com.dbpersis.service.PojoDataSet;
import com.dbpersis.service.QueryService;
import com.dbpersis.starter.DBPersisConfig;
import com.dbpersis.utils.BeanHandler;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ResourceUtils;

@ConfigurationProperties("com.terry.starter.service")
public class DbsersisService {

	private QueryService queryService = new QueryService();
	private PojoDataSet pojoDataSet = new PojoDataSet();

	private String driver;
	private String url;
	private String username;
	private String password;
	private int maxActive;

	public DbsersisService(String driver, String url, String username, String password, int maxActive) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.maxActive = maxActive;

		try {
			DBPersisConfig.ConfigDBPool(driver, url, username, password, maxActive);
			DBPersisConfig.ConfigQueryBase(ResourceUtils.getFile("classpath:query").getPath(),
					ResourceUtils.getFile("classpath:association").getPath());
			ResourceUtils.getFile("classpath:query").getPath();
			ResourceUtils.getFile("classpath:association").getPath();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public <T> List<T> queryService(String sqlName, BeanHandler<T> beanListHandler, Map<String, Object> params,
			int pageIndex, int pageSize) throws Exception {
		return queryService.query(sqlName, beanListHandler, params, pageIndex, pageSize);
	}

	public Serializable save(Object object) throws Exception {
		return pojoDataSet.save(object);
	}

	public Serializable update(Object object, List<String> param) throws Exception {
		return pojoDataSet.update(object, param);
	}
}
