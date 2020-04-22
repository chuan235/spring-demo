package com.gc.expexcel.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间字符串相关工具类
 */
public class OtherUtil{
	
	/**
	 * 将一个日期转换成String
	 */
	public static String formatDateToString(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	/**
	 * 将日期字符串转换成Date 方法名：getDateString<BR>
	 */
	public static Date parseStringToDate(String dateString, String pattern)
			throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(dateString);
	}

	/**
	 * 
	 * 将小数格式化成字符串，会进行四舍五入 如：3656.4554===结果:3656.46<BR>
	 */
	public static String formatDoubleToString(double dou,String format) {
		if(isNull(format)){
			format = "#.##";
		}
		DecimalFormat decimalFormat = new DecimalFormat(format);
		// 四舍五入，逢五进一
		String string = decimalFormat.format(dou);
		return string;
	}


	/**
	 * 判断字符串是否为空
	 */
	public static boolean isNull(String str) {
		return null == str || str.length() == 0 || "".equals(str)
				|| str.matches("\\s*");
	}

	/**
	 * 非空判断
	 */
	public static boolean isNotNull(String str) {
		return !isNull(str);
	}
	
	
	/**
	 * 百分比转换
	 */
	public static String getPercent(int num,double totalCount,String...objects){
		String format = "#.##";
		if(objects!=null && objects.length>0){
			format = objects[0];
		}
		return OtherUtil.formatDoubleToString((num/totalCount)*100,format)+"%";
	}
	
	/**
	 * 百分比转换
	 */
	public static String getPercent(int num,float totalCount,String...objects){//动态参数
		String format = "#.##";
		if(objects!=null && objects.length>0){
			format = objects[0];
		}
		return OtherUtil.formatDoubleToString((num/totalCount)*100,format)+"%";
	}
	
	
	
	/**
	 * 冒泡排序方法,如果为true那就是降序，false那么久是升序
	 */
	public static int[] sorts(int[] datas,boolean flag){
		int len = datas.length;
		// 轮询次数
		for (int i = 0; i < len; i++) {
			// 交换次数
			for(int j=0; j < len-1; j++){
				if(flag){ 
					if(datas[j] < datas[j+1]){
						int temp = datas[j];
						datas[j] = datas[j+1];
						datas[j+1] = temp;
					}
				}else{
					if(datas[j] < datas[j+1]){
						int temp = datas[j];
						datas[j] = datas[j+1];
						datas[j+1] = temp;
					}
				}
			}
		}
		return datas;
	}
	
	/**
	 * 凯德加密
	 */
	public static String encryption(String str,int k){
		String string = "";
		for (int i = 0; i < str.length(); i++) {
			char c= str.charAt(i);
			if(c>='a' && c<='z'){
				c += k%26;
				if(c<'a'){
					c+=26;
				}
				if(c>'z'){
					c-=26;
				}
			}else if(c>='A' && c<='Z'){
				c+=k%26;
				if(c<'A'){
					c+=26;
				}
				if(c>'Z'){
					c-=26;
				}
			}
			string+=c;
		}
		return string;
	}
	
	/**
	 * 凯德解密
	 */
	public static String dencryption(String str,int n){
		String string = "";
		int k = Integer.parseInt("-"+n);
		for (int i = 0; i < str.length(); i++) {
			char c= str.charAt(i);
			if(c>='a' && c<='z'){
				c += k%26;
				if(c<'a'){
					c+=26;
				}
				if(c>'z'){
					c-=26;
				}
			}else if(c>='A' && c<='Z'){
				c+=k%26;
				if(c<'A'){
					c+=26;
				}
				if(c>'Z'){
					c-=26;
				}
			}
			string+=c;
		}
		return string;
	}
}