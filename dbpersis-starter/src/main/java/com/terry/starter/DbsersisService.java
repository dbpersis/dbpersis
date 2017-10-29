package com.terry.starter;

import com.dbpersis.service.MyDataSource;
import com.dbpersis.service.PojoDataSet;
import com.dbpersis.service.QueryService;
import com.dbpersis.starter.DBPersisConfig;
import com.dbpersis.utils.BeanHandler;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

@ConfigurationProperties("com.terry.starter.service")
public class DbsersisService {

<<<<<<< HEAD
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
=======
  private String driver;
  private String url;
  private String username;
  private String password;
  private int maxActive;

  // Service
  private QueryService queryService;
  private PojoDataSet pojoDataSet;

  public DbsersisService(String driver, String url, String username, String password,
      int maxActive, MyDataSource dataSource) {
    Assert.notNull(dataSource, "DataSource can not be null");
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
      e.printStackTrace();
    }
    this.pojoDataSet = new PojoDataSet(dataSource);
    this.queryService = new QueryService(dataSource);
  }

  public <T> List<T> queryService(String sqlName, BeanHandler<T> beanListHandler,
      Map<String, Object> params, int pageIndex, int pageSize) throws Exception {
    return new QueryService().query(sqlName, beanListHandler, params, pageIndex, pageSize);
  }

  public Serializable save(Object object) throws Exception {
    return new PojoDataSet().save(object);
  }

  public Serializable update(Object object, List<String> param) throws Exception {
    return new PojoDataSet().update(object, param);
  }
>>>>>>> d70680c3286d30f5dc4ffb86b7f0ee27d967f612
}
