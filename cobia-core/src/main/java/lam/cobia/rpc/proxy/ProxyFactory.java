package lam.cobia.rpc.proxy;

import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.Provider;
import lam.cobia.spi.Spiable;

/**
* <p>
* proxy factory
* </p>
* @author linanmiao
* @date 2017年12月19日
* @version 1.0
*/
@Spiable("jdk")
public interface ProxyFactory {
	
	//====================
	public <T> T getConsumerProxy(Consumer<T> consumer);
	
	public <T> Provider<T> getProvider(T ref, Class<T> clazz);

}
