package com.example.demo.service;

import com.example.demo.bean.User;
import com.terry.starter.DbsersisService;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {
<<<<<<< HEAD
	@Autowired
    private DbsersisService exampleService;
	
	@Transactional
	public void input() throws Exception{
		long d1=System.currentTimeMillis();
		for(int i=0;i<10;i++){
			User sc = new User();
			sc.setId(String.valueOf(i)+"aaa");
			sc.setAge(19);
			sc.setUsername("User");
			sc.setPwd("21232f297a57a5a743894a0e4a801fc3");
			sc.setCreatedate(new Date(System.currentTimeMillis()));
			if(i>5){
				
				throw new Exception("");
			}
			exampleService.save(sc);
		}
        
	}
=======

  @Autowired
  private DbsersisService exampleService;

  @Transactional
  public void input() {
    long d1 = System.currentTimeMillis();
    for (int i = 0; i < 50; i++) {
      User sc = new User();
      sc.setId(String.valueOf(i) + "aaa");
      sc.setAge(19);
      sc.setUsername("User");
      sc.setPwd("21232f297a57a5a743894a0e4a801fc3");
      sc.setCreatedate(new Date(System.currentTimeMillis()));
      if (i > 10) {
        throw new RuntimeException("Failed insert user, roll back.");
      }
      try {
        exampleService.save(sc);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
>>>>>>> d70680c3286d30f5dc4ffb86b7f0ee27d967f612
}
