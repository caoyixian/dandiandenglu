package com.haitai.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginClass {
	@RequestMapping("/sayHello")
	@ResponseBody
	public String sayHello(){
		return "hello springboot";
	}
	
	@RequestMapping("/home")
	public String toHome(){
		return "home";
	}
	
	
	
	
	
	
	
	
	
	
	
}
