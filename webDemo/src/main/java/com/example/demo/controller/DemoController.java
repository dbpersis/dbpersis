package com.example.demo.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;

@RestController
@RequestMapping(value="/demo")
public class DemoController {
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
    }

}
