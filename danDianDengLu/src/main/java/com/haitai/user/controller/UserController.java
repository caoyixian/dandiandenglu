package com.haitai.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haitai.user.pojo.User;
import com.haitai.user.service.impl.UserServiceImpl;
import com.haitai.util.ItdragonResult;

@Controller
@RequestMapping("/userController")
public class UserController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	/**
	 * 用户登录接口
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/userLogin")
	@ResponseBody
	public ItdragonResult userLogin(String username, String password,
		    HttpServletRequest request, HttpServletResponse response){
		try{
			ItdragonResult result = userServiceImpl.loginUser(username, password, request, response);
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return ItdragonResult.build(500, e.getMessage());
		}
	}
	
	/**
	 * 用户登出功能，只需要删除掉redis数据库中token对应的key
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/logout/{token}")
	public String logout(@PathVariable String token){
		userServiceImpl.logout(token);//逻辑是从redis中删除key，实际情况下请和业务逻辑相结合
		return "index";
	}
	
	
	@RequestMapping(value="/getUserByToken/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token){
		ItdragonResult result = null;
		try{
			result = userServiceImpl.queryUserByToken(token);
		}catch(Exception e){
			e.printStackTrace();
			result = ItdragonResult.build(500, "");
		}
		return result;
	}
	
	

	@RequestMapping("/checkLogin") 
	public String checkLogin(String redirectUrl, HttpSession session, Model model){ 

		//1.判断是否有全局的会话 
		//从会话中获取令牌信息,如果取不到说明没有全局会话,如果能取到说明有全局会话 
		String token = (String) session.getAttribute("token"); 
		if(StringUtils.isEmpty(token)){ 
			//表示没有全局会话
			model.addAttribute("redirectUrl",redirectUrl); 
			//跳转到统一认证中心的登陆页面.已经配置视图解析器, 
			// 会找/WEB-INF/views/login.jsp视图 
			return "login"; 
		}else{ 
			/**---------------------------阶段三添加的代码start--------------------**/ 
			//有全局会话 //取出令牌信息,重定向到redirectUrl,把令牌带上  
			// http://www.wms.com:8089/main?token= 
			model.addAttribute("token",token); 
			/**---------------------------阶段三添加的代码end-----------------------**/ 
			return "redirect:"+redirectUrl; 
		} 
		
		

	}
	
	

/** 
 * * 校验客户端传过来的令牌信息是否有效 
 * * @param token 客户端传过来的令牌信息 
 * * @return 
 * */
	@RequestMapping("/verifyToken") 
	@ResponseBody 
	public String verifyToken(String token){ 
		//根据这个token码，是否能够查到用户信息
		User user = userServiceImpl.queryUserObjectByToken(token);
		if(user != null){
			return "true";
		}
		return "false";
	}


	
}
