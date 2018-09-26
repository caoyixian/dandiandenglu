package com.haitai.user.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.haitai.redis.JedisClient;
import com.haitai.user.pojo.User;
import com.haitai.user.repository.UserRepository;
import com.haitai.util.CookieUtil;
import com.haitai.util.ItdragonResult;
import com.haitai.util.JsonUtil;
import com.haitai.util.StringUtil;

@Service
@Transactional//springdata jpa 默认所有的事务都是只读的，因为在repository层有modify所以此处要添加次注解
@PropertySource(value="classpath:redis.properties")
public class UserServiceImpl {
	
	@Autowired
	private UserRepository userRepository;
	/*@Autowired
	private JedisClient jedisClient;*/
	
	 @Value("${REDIS_USER_SESSION_KEY}")
	 private String REDIS_USER_SESSION_KEY;
	 
	 @Value("${SSO_SESSION_EXPIRE}")
	 private Integer SSO_SESSION_EXPIRE;

	 @Autowired
	 JedisClient jedisClient;
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public ItdragonResult registerUser(User user){
		//检查此用户名是否已经被注册过了，一般在前端页面注册时候完成，因为注册不涉及高并发，再加一层也不影响性能
		if(null != userRepository.findByAccount(user.getAccount())){//说明用户被注册了
			return ItdragonResult.build(400, "");
		}
		userRepository.save(user);
		//注册成功后发邮件激活，现在都是短信发送验证码
		return ItdragonResult.build(200, "");
	}
	
	/**
	 * 修改用户的邮箱
	 * @param email
	 * @return
	 */
	public ItdragonResult editUserEmail(String email){
		//通过session获取用户的信息，这里暂时写死，后面做单点登录时再修改此处的代码
		
		long id = 3L;
		//添加一些验证，比如短信验证
		userRepository.updateUserEmail(id, email);
		return ItdragonResult.ok();
	}
	
	
	public ItdragonResult loginUser(String account, String password,
            HttpServletRequest request, HttpServletResponse response){
		 // 判断账号密码是否正确
        User user = userRepository.findByAccount(account);
        if (!ItdragonResult.decryptPassword(user, password)) {
            return ItdragonResult.build(400, "账号名或密码错误");
        }
     // 生成token
        String token = UUID.randomUUID().toString();
     // 清空密码和盐避免泄漏
        String userPassword = user.getPassword();
        String userSalt = user.getSalt();
        user.setPassword(null);
        user.setSalt(null);
     // 把用户信息写入 redis
        jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtil.objectToJson(user));
     // user 已经是持久化对象，被保存在session缓存当中，若user又重新修改属性值，那么在提交事务时，此时 hibernate对象就会拿当前这个user对象和保存在session缓存中的user对象进行比较，如果两个对象相同，则不会发送update语句，否则会发出update语句。
        user.setPassword(userPassword);
        user.setSalt(userSalt);
        // 设置 session 的过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
     // 添加写 cookie 的逻辑，cookie 的有效期是关闭浏览器就失效。
        CookieUtil.setCookie(request, response, "USER_TOKEN", token);
     // 返回token
        return ItdragonResult.ok(token); 
	}
	
	/**
	 * 系统登出时候需要将redis数据库中的session数据删除掉
	 * @param token
	 */
	public void logout(String token) {
		jedisClient.del(REDIS_USER_SESSION_KEY + ":" + token);
    }
	
	/**
	 * 根据token查找用户，确定该用户是否处于登录状态
	 * @param token
	 * @return
	 */
	public ItdragonResult queryUserByToken(String token) {
        // 根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        // 判断是否为空
        if (StringUtil.isEmpty(json)) {
            return ItdragonResult.build(400, "此session已经过期，请重新登录");
        }
        // 更新过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
        // 返回用户信息
        return ItdragonResult.ok(JsonUtil.jsonToPojo(json, User.class));
    }
	
	
	public User queryUserObjectByToken(String token) {
		// 根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        User user = null;
        if(!StringUtil.isEmpty(json)){//此时说明根据key能够在redis数据库中找到对应的用户
        	user = 	JsonUtil.jsonToPojo(json, User.class);
        }
        return user;
	}
	
	
}
