package com.haitai.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.IdleConnectionEvictor;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

/**
 * @Configuration  作用于类上，指明该类相当于一个xml配置文件
 * @PropertySource  作用于类上，指明要读取的配置文件的位置  当有当过配置文件需要读取时，
 * value属性的格式为value={“xxx:xxx”,”xxx:xxx”},属性ignoreResourceNotFound=true 作用是文件不存在时忽略 
 * 
 * @Bean           作用于方法上，指明该方法相当于xml文件中的<bean>
 * @Value          将配置文件中的值付给该属性
 * @author yixian.cao
 *
 */
@Configuration
@PropertySource(value="classpath:httpclient.properties")
public class HttpClientPropeties {
	
	@Value("${http.maxTotal}")
	private Integer maxTotal;
	
	@Value("${http.defaultMaxPerRoute}")
	private Integer defaultMaxPerRoute;
	
	@Value("${http.connectTimeout}")
	private Integer connectTimeout;
	
	@Value("${http.connectionRequestTimeout}")
	private Integer connectionRequestTimeout;
	
	@Value("${http.socketTimeout}")
	private Integer socketTimeout;
	
	@Autowired//此处注入的值，为下面@bean注解方法的返回值
    private PoolingHttpClientConnectionManager manager;
	
	@Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		// 最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
        // 每个主机的最大并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		return poolingHttpClientConnectionManager;
	}
	
	@Bean   // 定期清理无效连接
    public IdleConnectionEvictor idleConnectionEvictor() {
        return new IdleConnectionEvictor(manager, 1L, TimeUnit.HOURS);
    }
	
	@Bean   // 定义HttpClient对象 注意该对象需要设置scope="prototype":多例对象
    @Scope("prototype")
    public CloseableHttpClient closeableHttpClient() {
        return HttpClients.custom().setConnectionManager(this.manager).build();
    }
	
	@Bean   // 请求配置
    public RequestConfig requestConfig() {
        return RequestConfig.custom().setConnectTimeout(connectTimeout) // 创建连接的最长时间
                .setConnectionRequestTimeout(connectionRequestTimeout) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(socketTimeout) // 数据传输的最长时间
                .build();
    }

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public PoolingHttpClientConnectionManager getManager() {
		return manager;
	}

	public void setManager(PoolingHttpClientConnectionManager manager) {
		this.manager = manager;
	}
	
	
	
	
}
