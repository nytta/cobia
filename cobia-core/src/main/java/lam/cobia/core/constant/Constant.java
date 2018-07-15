package lam.cobia.core.constant;
/**
* <p>
* config constant
* </p>
* @author linanmiao
* @date 2017年12月22日
* @version 1.0
*/
public class Constant {
	
	public static final String KEY_PREFIX = "cobia";
	
	public static final String KEY_PORT = KEY_PREFIX + ".port";
	
	//server config key:
	
	public static final String KEY_SERVER_HOST = KEY_PREFIX + ".server.host";
	
	//client config key:
	
	public static final String KEY_CLIENT_IS_SHARE = KEY_PREFIX + ".client.isShare";
	
	//default config value
	
	public static final String DEFAULT_SERVER_HOSTNAME = "127.0.0.1";
	
	public static final int DEFAULT_SERVER_PORT = 4325;
	
	public static final boolean DEFAULT_CLIENT_IS_SHARE = Boolean.TRUE.booleanValue();
	
	public static final String DEFAULT_CHART_UTF8 = "UTF-8";
	
	public static final long DEFAULT_TIMEOUT = 1000L;

}
