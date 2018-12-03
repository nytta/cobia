package lam.cobia.rpc.support;

import java.io.Closeable;

/**
* <p>
* exporter
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public interface Exporter<T> extends Closeable{
	
	public String getKey();
	
	public Provider<T> getProvider();
	
	public void close();

}
