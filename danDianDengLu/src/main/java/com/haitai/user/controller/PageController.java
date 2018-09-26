package com.haitai.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/pageController")
public class PageController {
	
	@RequestMapping("/showLogin")
	public String showLogin(String redirect, Model model){
		model.addAttribute("redirect", redirect);//moel用来封装返回前台的数据
		return "login";
	}
}
