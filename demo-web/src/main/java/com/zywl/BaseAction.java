package com.zywl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Index")
public class BaseAction {
	
	@RequestMapping("index.do")
	public String test(String name){
		System.out.println("ssssssssssss");
		return "index";
	}
}
