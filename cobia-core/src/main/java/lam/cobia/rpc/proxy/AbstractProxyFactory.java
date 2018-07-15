package lam.cobia.rpc.proxy;

import lam.cobia.rpc.Consumer;

/**
* <p>
* abstract proxy factory
* </p>
* @author linanmiao
* @date 2017年12月19日
* @version 1.0
*/
public abstract class AbstractProxyFactory implements ProxyFactory{
	
	@Override
	public <T> T getConsumerProxy(Consumer<T> consumer) {
		Class<?>[] interfaces = new Class<?>[] {consumer.getInterface()};
		return getConsumerProxy(consumer, interfaces);
	}
	
	public abstract <T> T getConsumerProxy(Consumer<T> consumer, Class<?>[] interfaces);

}
