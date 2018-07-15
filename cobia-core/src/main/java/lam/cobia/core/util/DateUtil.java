package lam.cobia.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
* <p>
* 日期工具类
* </p>
* @author linanmiao
* @date 2016年9月28日
* @versio 1.0
*/
public class DateUtil {
	
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss SSS";
	
	public static String getCurrentTimeSSS(){
		return getCurrentTime(FORMAT_YYYY_MM_DD_HH_MM_SS_SSS);
	}
	
	public static String getCurrentTime(){
		return getCurrentTime(FORMAT_YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String getCurrentTime(String format){
		return getCurrentTime(new Date(), format);
	}
	
	public static String getCurrentTime(Date date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	public static Date toDate(String source, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date add(Date date, int field, int amount){
		Calendar calendar = addToCalendar(date, field, amount);
		return calendar.getTime();
	}
	
	public static Calendar addToCalendar(Date date, int field, int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar;
	}
	
	public static Date set(Date date, int field, int value){
		Calendar calendar = setToCalendar(date, field, value);
		return calendar.getTime();
	}
	
	public static Calendar setToCalendar(Date date, int field, int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(field, value);
		return calendar;
	}
	
	public static void main(String[] args) {
		Date now = new Date();
		System.out.println(now = DateUtil.set(now, Calendar.HOUR_OF_DAY, 0));
		System.out.println(now = DateUtil.set(now, Calendar.MINUTE, 0));
		System.out.println(now = DateUtil.set(now, Calendar.SECOND, 0));
		System.out.println(now = DateUtil.add(now, Calendar.DATE, 1));
	}

}
