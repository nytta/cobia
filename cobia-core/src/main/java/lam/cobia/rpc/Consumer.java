package lam.cobia.rpc;

import java.io.Closeable;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月6日
* @versio 1.0
*/
public interface Consumer<T> /*extends Invoker*/ extends Closeable{
	
	public String getKey();
	
	public Class<T> getInterface();
	
	public Result invoke(Invocation invocation);
	
	public void close();

}
