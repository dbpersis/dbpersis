/**
 * @Date 2017年10月23日
 * @author terry
 */
package com.dbpersis.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;


public class Converter<T> {

  public static <T> List<T> convert2BeanList(ResultSet rs, Class<T> bean) throws Exception {
    Field[] fields = bean.getDeclaredFields();
    List<T> lists = new ArrayList<T>();
    while (rs.next()) {
      T obj = bean.newInstance();
      for (Field field : fields) {
        if (field.getType().getSimpleName().equals("String") || field.getType().getSimpleName()
            .equals("Date") || field.getType().getSimpleName().equals("int")) {
          String pname = field.getName();
          BeanUtils.setProperty(obj, pname, rs.getObject(pname));
        }
      }
      lists.add(obj);
    }
    return lists != null ? lists : null;
  }

}
