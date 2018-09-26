package com.haitai.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 类中所有的方法都是静态，不允许创建对象
 * @author yixian.cao
 *
 */
@Component
public class JedisClient {
	
	private static Logger logger = LoggerFactory.getLogger(JedisClient.class);
	
	
	//private JedisClient(){}
	@Autowired
	JedisPool jedisPool;
	
	/**
	 * 向redis数据库中保存数据
	 * @param key
	 * @param data
	 */
	public void set(String key,String data){
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.set(key, data);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("连接redis服务失败");
		}finally{//必须要要把jedis连接对象归还给连接池
			jedis.close();
		}
		
	}
	
	/**
	 * 根据key从redis数据库中获取数据
	 * @param key
	 * @return
	 */
	public String get(String key){
		Jedis jedis = null;
		String data = null;
		try{
			jedis =jedisPool.getResource();
			data = jedis.get(key);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("连接redis服务失败");
		}finally{
			jedis.close();
		}
		return data;
	}
	
	/**
	 * 根据key删掉redis数据库中的数据
	 * @param key
	 */
	public void del(String key){
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.del(key);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("redis数据库连接失败");
		}finally{
			jedis.close();
		}
	}
	
	/**
	 * 设置key的过期时间
	 * @param key
	 * @param time
	 */
	public void expire(String key,long time){
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.expireAt(key, time);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("连接redis服务失败");
		}finally{
			jedis.close();
		}
	}
}
