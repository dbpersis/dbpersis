package com.ge.Demo;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbpersis.service.PojoDataset;
import com.dbpersis.service.QueryService;
import com.dbpersis.starter.DBPersisConfig;
import com.dbpersis.utils.BeanHandler;
import com.ge.bean.SchoolClass;
import com.ge.bean.Students;
import com.ge.bean.User;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	new App().testBase();
    }
    public void testBase(){
    	String driver="com.mysql.cj.jdbc.Driver";
		String url="jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String username="root";
		String password="123123";
		String maxActive="5";	
		
		try {
			DBPersisConfig.ConfigDBPool(driver, url, username, password, maxActive);
			DBPersisConfig.ConfigQueryBase("src/main/resources/query", "src/main/resources/association");
			QueryService queryService=new QueryService();
			PojoDataset dataset = new PojoDataset();
			User sc = new User();
			sc.setId("aaaads");
			sc.setAge(19);
			sc.setUsername("User");
			sc.setPwd("21232f297a57a5a743894a0e4a801fc3");
			sc.setCreatedate(new Date(System.currentTimeMillis()));
			dataset.save(sc);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", "1");
			List<SchoolClass> scList = queryService.query("findClass", new BeanHandler(SchoolClass.class), params,0,0);
			
			List<Students> stud = scList.get(0).getStudents();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
