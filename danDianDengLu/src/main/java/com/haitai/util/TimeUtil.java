package com.haitai.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取日期类型字符串的类
 * @author yixian.cao
 *
 */
public class TimeUtil {
	private TimeUtil(){}//工具类所有的方法都是静态方法，不允许穿件对象
	public static String getCurrentDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String time = format.format(date);
		return time;
	}
	
}
