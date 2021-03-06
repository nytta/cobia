package lam.cobia.rpc.support;

import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.Parameterable;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Result;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月6日
* @versio 1.0
*/
public interface Consumer<T> extends Parameterable {
	
	public String getKey();

	public RegistryData getRegistryData();
	
	public Class<T> getInterface();
	
	public Result invoke(Invocation invocation);
	
	public void close();

}
