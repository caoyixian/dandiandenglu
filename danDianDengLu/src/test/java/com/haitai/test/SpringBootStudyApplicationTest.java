package com.haitai.test;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.haitai.redis.JedisClient;
import com.haitai.user.pojo.User;
import com.haitai.user.repository.UserRepository;
import com.haitai.user.service.impl.UserServiceImpl;
import com.haitai.util.TimeUtil;

/**
 * @RunWith 它是一个运行器
 * @RunWith(SpringRunner.class) 表示让测试运行于Spring测试环境，不用启动spring容器即可使用Spring环境
 * @SpringBootTest(classes=StartApplication.class)  表示将StartApplication.class纳入到测试环境中，若不加这个则提示bean找不到。
 * 
 * @author yixian.cao
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootStudyApplicationTest {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private UserRepository userRepository;
	
	@Test        // 测试注册，新增数据
	public void registerUser(){
		User user = new User();
        user.setAccount("cyx");
        user.setUserName("ITDragonGit");
        user.setEmail("itdragon@git.com");
        user.setIphone("12349857999");
        user.setPassword("123456");;
        user.setPlainPassword("adminroot");
        user.setPlatform("github");
        user.setCreatedDate(TimeUtil.getCurrentDate());
        user.setUpdatedDate(TimeUtil.getCurrentDate());
        //ItdragonUtils.entryptPassword(user);
        userServiceImpl.registerUser(user);
	}
	
	 @Test   // 测试SpringData 关键字
	    public void findByEmailEndingWithAndCreatedDateLessThan() {
	        List<User> users = userRepository.findByEmailEndingWithAndCreatedDateLessThan("qq.com", TimeUtil.getCurrentDate());
	        System.out.println(users.toString());
	    }
	    
	    @Test   // 测试SpringData @Query 注解和传多个参数
	    public void getActiveUserCount() {
	        long activeUserCount = userRepository.getActiveUserCount("weixin", TimeUtil.getCurrentDate());
	        System.out.println(activeUserCount);
	    }
	    
	    @Test   // 测试SpringData @Query 注解，传多个参数 和 like 查询
	    public void findByEmailAndIhpneLike() {
	        List<User> users = userRepository.findByEmailAndIhpneLike("163.com", "6666");
	        System.out.println(users.toString());
	    }
	    
	    @Test   // 测试SpringData @Query 注解 和 @Modifying 注解
	    public void updateUserEmail() {
	        /**
	         * org.springframework.dao.InvalidDataAccessApiUsageException:Executing an update/delete query; nested exception is javax.persistence.TransactionRequiredException: Executing an update/delete query
	         * userRepository.updateUserEmail(3L, "update@email.com");
	         */
	        userServiceImpl.editUserEmail("update@email.com");
	    }
	    
	    @Test   // 测试SpringData PagingAndSortingRepository 接口
	    public void testPagingAndSortingRepository() {
	        int page = 1;   // 从0开始，第二页
	        int size = 3;   // 每页三天数据
	        PageRequest pageable = new PageRequest(page, size, new Sort(new Order(Direction.ASC, "id")));
	        Page<User> users = userRepository.findAll(pageable);
	        System.out.println(users.getContent().toString()); // 当前数据库中有5条数据，正常情况可以打印两条数据，id分别为4，5 (先排序，后分页)
	    }
	    
	  /*  @Test   // 测试SpringData JpaSpecificationExecutor 接口
	    public void testJpaSpecificationExecutor(){
	    	int pageNo = 1;
	        int pageSize = 3;
	        PageRequest pageable = new PageRequest(pageNo, pageSize);
	        Specification<User> specification = new Specification<User>() {

				@Override
				public Predicate toPredicate(Root<User> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					//Predicate predicate = cb.gt(root.get("id"),1); // 查询id 大于 1的数据
					Predicate predicate = cb.gt(root.get("id"), 1);
	                return predicate;
				}
	        	
	        };
	    	
	        Page<User> users = userRepository.findAll(specification, pageable);
	        System.out.println(users.getContent().toString());  // 当前数据库中有5条数据，正常情况可以打印一条数据，id为5
	    }*/
	
	
	    @Autowired
	    JedisClient jedisClient;
	    @Test
	    public void testGetJedis(){
	    	String uuid = UUID.randomUUID().toString();
	    	//Jedis jedis = jedisPool.getResource(); 
	    	//jedis.setex("ssssssss", 1000, uuid);
	    	jedisClient.set("ww", "123456");
	    }
	
	
}
