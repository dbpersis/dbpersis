/**
 * @Date 2017年10月23日
 * @author terry
 */

package com.dbpersis.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class BeanHandler<T> {

  private final Class<T> type;

  public BeanHandler(Class<T> type) {
    this.type = type;
  }

  public Class<T> getType() {
    return type;
  }

  public List<T> handle(ResultSet rs) throws SQLException {
    try {
      return rs != null ? Converter.convert2BeanList(rs, this.type) : null;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

}
