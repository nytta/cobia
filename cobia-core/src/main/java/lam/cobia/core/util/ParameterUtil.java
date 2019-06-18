package lam.cobia.core.util;

import org.apache.commons.lang3.BooleanUtils;

import java.util.Map;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年6月29日
* @version 1.0
*/
public class ParameterUtil {

	public static String getConfigParameter(String key, Map<String, Object> map, String defaultValue) {
		String value = getConfigParameter(key, map);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static String getConfigParameter(String key, Map<String, Object> map) {
		Object value = map.get(key);
		if (value == null) {
			return getParameter(key);
		}
		//get system config value of key, when value is null of key in map.
		return String.valueOf(value);
	}
	
	public static String getParameter(String key) {
		//priority level:env->java command->configure
		String value = System.getenv(key);
		if (value != null) {
			return value;
		}
		value = SystemProperties.getProperty(key);
		if (value != null) {
			return value;
		}
		//@TODO get parameter from configure
		//value = value of configure..
		return value;
	}
	
	public static int getParameterInt(String key) {
		String value = getParameter(key);
		return Integer.parseInt(value);
	}
	
	public static String getParameter(String key, String defaultValue) {
		String value = getParameter(key);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}
	
	public static int getParameterInt(String key, int defaultValue) {
		try {
			String value = getParameter(key);
			if (value == null) {
				return defaultValue;
			}
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int getParameterInt(String key, Map<String, Object> params, int defaultValue) {
		String value = getParameter(key);
		if (value != null) {
			return Integer.parseInt(value);
		}
		Object vObject = params.get(key);
		if (vObject != null) {
			return Integer.parseInt(vObject.toString());
		}
		return defaultValue;
	}

	
	public static boolean getParameterBoolean(String key) {
		String value = getParameter(key);
		return Boolean.parseBoolean(value);
	}
	
	public static boolean getParameterBoolean(String key, boolean defaultValue) {
		try {
			String value = getParameter(key);
			if (value == null) {
				return defaultValue;
			}
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean getParameterBoolean(String key, Map<String, Object> params, boolean defaultValue) {
		try {
			String value = getParameter(key);
			if (value == null) {
				return defaultValue;
			}
			final Object obj = params.get(key);
			if (obj == null) {
				return Boolean.parseBoolean(value);
			}
			return BooleanUtils.toBoolean(obj.toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
