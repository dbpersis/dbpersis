/**
 * @Date 2017年10月23日
 * @author terry
 */
package com.dbpersis.service;

import java.io.Serializable;
import java.util.List;

public class PojoDataSet {
  private MyDataSource dataSource;

  public PojoDataSet() {
    dataSource = new MyDataSource();
  }

  public PojoDataSet(MyDataSource dataSource) {
    if (dataSource == null) {
      throw new RuntimeException("DataSource can not be null");
    }
    this.dataSource = dataSource;
  }

  public Serializable save(Object object) throws Exception {
    dataSource.save(object);
    return null;
  }

  public Serializable update(Object object, List<String> param) throws Exception {
    dataSource.update(object, param);
    return null;
  }
}
