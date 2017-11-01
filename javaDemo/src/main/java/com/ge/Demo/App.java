package com.ge.Demo;

import com.dbpersis.service.PageService;
import com.dbpersis.service.PojoDataSet;
import com.dbpersis.service.QueryService;
import com.dbpersis.starter.DBPersisConfig;
import com.dbpersis.utils.BeanHandler;
import com.dbpersis.utils.Page;
import com.ge.bean.Organization;
import com.ge.bean.SchoolClass;
import com.ge.bean.Students;
import com.ge.bean.User;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    new App().testBase();
  }

  public void testBase() {
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String username = "root";
    String password = "123123";
    int maxActive = 5;

    try {
      DBPersisConfig.ConfigDBPool(driver, url, username, password, maxActive);
      DBPersisConfig.ConfigQueryBase("src/main/resources/query", "src/main/resources/association");
      QueryService queryService = new QueryService();
      PojoDataSet dataSet = new PojoDataSet();
      PageService pageService = new PageService();
      Map<String, Object> params = new HashMap<String, Object>();
      //params.put("id", "5");
      //Organization org = queryService.querySingle("findOrganization", new BeanHandler(Organization.class), params);
      Page sList = pageService.queryForPage("findUser", new BeanHandler(User.class), params, 5, 20);
      List<SchoolClass> scList = queryService
          .query("findClass", new BeanHandler(SchoolClass.class), params, 0, 0);
      

      //List<Students> stud = scList.get(0).getStudents();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
