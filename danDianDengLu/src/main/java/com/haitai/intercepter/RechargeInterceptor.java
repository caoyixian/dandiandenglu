package com.haitai.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.haitai.user.pojo.User;
import com.haitai.user.service.impl.UserServiceImpl;
import com.haitai.util.CookieUtil;
import com.haitai.util.StringUtil;

/**
 * 该类为自定义的一个拦截器，用来专门拦截用户的请求，能在三个时间点进行拦截
 * @author yixian.cao
 *
 */
public class RechargeInterceptor implements HandlerInterceptor{
	
	 public static final String COOKIE_NAME = "USER_TOKEN";
	 
	    @Autowired
	    private UserServiceImpl userServiceImpl;

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("请求到达controller之前进行拦截");
		
		  String token = CookieUtil.getCookieValue(request, COOKIE_NAME);
	        User user = this.userServiceImpl.queryUserObjectByToken(token);
	        if (StringUtil.isEmpty(token) || null == user) {
	            // 跳转到登录页面，把用户请求的url作为参数传递给登录页面。
	            response.sendRedirect("http://localhost:8082/pageController/showLogin?redirect=" + request.getRequestURL());
	            // 返回false
	            return false;
	        }
	        // 把用户信息放入Request
	        request.setAttribute("user", user);
	        // 返回值决定handler是否执行。true：执行，false：不执行。
	        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("请求处理完成之后视图渲染之前进行拦截");
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("完成视图渲染之后进行拦截");
		
	}

}
