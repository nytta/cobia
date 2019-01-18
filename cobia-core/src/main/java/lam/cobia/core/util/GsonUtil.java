package lam.cobia.core.util;

import com.google.gson.GsonBuilder;

/**
* <p>
* gson util class
* </p>
* @author linanmiao
* @date 2018年4月30日
* @versio 1.0
*/
public class GsonUtil {

	private static GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

	private static GsonBuilder notNullGsonBuilder = new GsonBuilder();
	
	public static String toJson(Object src) {
		return gsonBuilder.create().toJson(src);
	}

	public static String toNotNullJson(Object src) {
		return notNullGsonBuilder.create().toJson(src);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gsonBuilder.create().fromJson(json, classOfT);
	}
	
}
