package com.haitai.util;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haitai.user.pojo.User;

/**
 * 封装的一个返回结果类，当做任何方法的返回值，返回给前台，进行固定处理
 * @author yixian.cao
 *
 */
public class ItdragonResult {
	//定义jackson对象
	private static final ObjectMapper MAPPER = new ObjectMapper ();
	//相应的状态码
	private Integer status;
	//相应的信息
	private String msg;
	//相应的数据
	private Object data;
	
	//返回信息最全的的结果对象
	public static ItdragonResult build(Integer status,String msg,Object data){
		return new ItdragonResult(status,msg,data);
	}
	//返回带有相应数据的结果对象
	public static ItdragonResult ok(Object data){
		return new ItdragonResult(data);
	}
	//返回不带相应数据的结果对象
	public static ItdragonResult ok(){
		return new ItdragonResult(null);
	}
	
	public ItdragonResult(){}
	
	public static ItdragonResult build(Integer status,String msg){
		return new ItdragonResult(status,msg,null);
	}
	
	public ItdragonResult(Integer status,String msg,Object data){
		this.data = data;
		this.msg = msg;
		this.status = status;
	}
	
	public ItdragonResult(Object data){
		this.status = 200;
		this.msg = "OK";
		this.data = data;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 将一定格式的字符串转封装到返回结果对象当中去
	 * @param jsonData
	 * @param clazz
	 * @return
	 */
	public static ItdragonResult formatToItdragonResult(String jsonData,Class<?> clazz){
		try{
			if(clazz == null){
				return MAPPER.readValue(jsonData, ItdragonResult.class);
			}
			 JsonNode jsonNode = MAPPER.readTree(jsonData);
	         JsonNode data = jsonNode.get("data");
	         Object obj = null;
	         if (clazz != null) {
	                if (data.isObject()) {
	                    obj = MAPPER.readValue(data.traverse(), clazz);
	                } else if (data.isTextual()) {
	                    obj = MAPPER.readValue(data.asText(), clazz);
	                }
	            }
	            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 当没有object对象的转化时
	 * @param json
	 * @return
	 */
	public static ItdragonResult format(String json){
		try{
			return MAPPER.readValue(json, ItdragonResult.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将json数据转换成有泛型的list集合
	 * @param jsonData
	 * @param clazz
	 * @return
	 */
	public static ItdragonResult formatToList(String jsonData,Class<?> clazz){
		
		try{
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if(data.isArray() && data.size()>0){
				obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
			}
			return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 判断登录的密码是否正确
	 * @param user
	 * @param password
	 * @return
	 */
	public static boolean decryptPassword(User user,String password){
		if(password == null || user==null ||user.getPassword()==null){
			return false;
		}
		return password.equals(user.getPassword());
	}
	
}
