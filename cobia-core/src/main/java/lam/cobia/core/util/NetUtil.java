package lam.cobia.core.util;

import java.net.InetSocketAddress;

/**
* <p>
* net util
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class NetUtil {
	
	public static String parseToString(InetSocketAddress address) {
		return address.getHostName() + ":" + address.getPort();
	}

}
