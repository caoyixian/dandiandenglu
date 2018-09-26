package com.haitai.intercepter;

import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 该类完成的主要功能为将之前自定义的拦截器注册到springboot中，让springboot工程启用这个拦截器
 * @author yixian.cao
 *
 */
public class WebAppConfig extends WebMvcConfigurerAdapter {
	
	 @Override
	 public void addInterceptors(InterceptorRegistry registry){//配置拦截器以及配置拦截的路径
		// registry.addInterceptor(new RechargeInterceptor()).addPathPatterns("/**");
		 
		 //上面的方法可以拆开了写，这样既可以设置拦截的路径，也可以设置不拦截的路径
		 InterceptorRegistration ir = registry.addInterceptor(new RechargeInterceptor());
		 ir.addPathPatterns("/**");//设置拦截器要进行拦截的路径
		 ir.excludePathPatterns("js/*");//设置拦截器要放行的路径
	 }
	
	
	
	
	
	
	
	
	
}
