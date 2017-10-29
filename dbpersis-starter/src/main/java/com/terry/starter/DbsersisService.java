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

  private QueryService queryService;
  private PojoDataSet pojoDataSet;

  public DbsersisService(String driver, String url, String username, String password,
      int maxActive, MyDataSource dataSource) {
    Assert.notNull(dataSource, "DataSource can not be null");
    try {
      DBPersisConfig.ConfigDBPool(driver, url, username, password, maxActive);
      DBPersisConfig.ConfigQueryBase(ResourceUtils.getFile("classpath:query").getPath(),
          ResourceUtils.getFile("classpath:association").getPath());
    } catch (FileNotFoundException e) {
      //e.printStackTrace();
    }
    this.pojoDataSet = new PojoDataSet(dataSource);
    this.queryService = new QueryService(dataSource);
  }

  public <T> List<T> queryService(String sqlName, BeanHandler<T> beanListHandler,
      Map<String, Object> params, int pageIndex, int pageSize) throws Exception {
    return queryService.query(sqlName, beanListHandler, params, pageIndex, pageSize);
  }

  public Serializable save(Object object) throws Exception {
    return pojoDataSet.save(object);
  }

  public Serializable update(Object object, List<String> param) throws Exception {
    return pojoDataSet.update(object, param);
  }
}
