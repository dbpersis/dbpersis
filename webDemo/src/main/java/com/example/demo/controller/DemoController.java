package com.example.demo.controller;

import com.example.demo.bean.User;
import com.terry.starter.DbsersisService;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;

@RestController
@RequestMapping(value = "/demo")
public class DemoController {
<<<<<<< HEAD
	@Autowired
    private DemoService exampleService;

    @RequestMapping(value="/input",method=RequestMethod.GET)
    public String input(String word){
    	
		try {
			exampleService.input();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return word;
=======

  @Autowired
  private DbsersisService exampleService;

  @Autowired
  private DemoService demoService;

  @RequestMapping(value = "input", method = RequestMethod.GET)
  public String input(String word) {
    try {
      User sc = new User();
      sc.setId("terry");
      sc.setAge(19);
      sc.setUsername("User");
      sc.setPwd("21232f297a57a5a743894a0e4a801fc3");
      sc.setCreatedate(new Date(System.currentTimeMillis()));
      exampleService.save(sc);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
>>>>>>> d70680c3286d30f5dc4ffb86b7f0ee27d967f612
    }
    return word;
  }

  @RequestMapping(value = "batch_input", method = RequestMethod.GET)
  public String batchInput() {
    demoService.input();
    return "SUCCESS";
  }
}
