package com.haitai.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource(value="classpath:redis.properties")
public class RedisProperties {
	
	    
	    @Value("${REDIS_USER_SESSION_KEY}")
	    private String REDIS_USER_SESSION_KEY;
	    
	    @Value("${redis.node.host}")
	    private String host;
	    @Value("${redis.node.port}")
	    private int port;
	    @Value("${redis.password}")
	    private String password;
	    @Value("${redis.server.maxTotal}")
	    private int maxTotal;
	    @Value("${redis.server.maxIdle}")
	    private int maxIdle;
	    @Value("${redis.server.maxWaitMillis}")
	    private int maxWaitMillis;
	    @Value("${redis.server.timeOut}")
	    private int timeOut;

	
	    
	    @Bean(name = "jedisPool")
	    public JedisPool jedisPool() {
	        JedisPoolConfig config = new JedisPoolConfig();
	        config.setMaxTotal(maxTotal);
	        config.setMaxIdle(maxIdle);
	        config.setMaxWaitMillis(maxWaitMillis);
	        return new JedisPool(config, host, port, timeOut, null);
	    }

	   /* @Bean
	    @ConditionalOnMissingBean(JedisClient.class)
	    public JedisClient redisClient(@Qualifier("jedisPool") JedisPool pool) {
	        JedisClient redisClient = new RedisClient();
	        redisClient.setJedisPool(pool);
	        return redisClient;
	    }*/

	    
	    
	    
	/*@Bean
	public JedisPool jedisPool(){ // 省略第一个参数则是采用 Protocol.DEFAULT_DATABASE
		//JedisPool jedisPool = new JedisPool(jedisPoolConfig(),redisNodeHost, redisNodePort);
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisMaxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWait);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisNodeHost, redisNodePort,
        		maxWait, password);

		return jedisPool;
	}
	
	@Bean
	public ShardedJedisPool shardedJedisPool(){
		List<JedisShardInfo> jedisShardInfos = new ArrayList<JedisShardInfo>();
		jedisShardInfos.add(new JedisShardInfo(redisNodeHost, redisNodePort));
        return new ShardedJedisPool(jedisPoolConfig(), jedisShardInfos);
	}
	
	@Autowired
	private static JedisPool jedisPool;
	
	*/
	    
	    
	
}
