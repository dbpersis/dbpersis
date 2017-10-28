package com.example.demo.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.User;
import com.terry.starter.DbsersisService;

@Service
public class DemoService {
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
}
