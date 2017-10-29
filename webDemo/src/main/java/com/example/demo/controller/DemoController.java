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
  @Autowired
  private DemoService demoService;


  @RequestMapping(value = "input", method = RequestMethod.GET)
  public String batchInput() {
    try {
		demoService.input();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return "SUCCESS";
  }
}
