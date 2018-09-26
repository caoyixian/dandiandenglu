package com.haitai.util;
/**
 *该工具类所有方法均为静态的，不允许new对象
 * @author yixian.cao
 *
 */
public class StringUtil {
	
	private StringUtil(){}
	
	public static boolean isEmpty(String data){
		if(data == null || "".equals(data)){
			return true;
		}
		return false;
	}
	
	
	
	
	
	
}
