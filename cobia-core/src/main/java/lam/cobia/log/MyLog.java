package lam.cobia.log;

import com.google.gson.Gson;

import lam.cobia.core.util.DateUtil;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月7日
* @versio 1.0
*/
public class MyLog {
	
	public static String timeBefore(String log){
		return String.format("%s:%s", DateUtil.getCurrentTimeSSS(), log);
	}
	
	private static String getMethodName() {
		Gson gson = new Gson();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTrace : stackTraces) {			
			System.out.println(gson.toJson(stackTrace));
		}
		return "";
	}
	
	public static StackTraceElement getCurrentStackTraceElement() {
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		return stackTraces[2];
	}
	
}
