package lam.cobia.core.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
* <p>
* gson util class
* </p>
* @author linanmiao
* @date 2018年4月30日
* @versio 1.0
*/
public class GsonUtil {

	private static Gson gson = new Gson();
	
	public static String toJson(Object src) {
		//return gson.toJson(src, new TypeToken<String>(){}.getType());
		return gson.toJson(src);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}
	
}
