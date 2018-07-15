package lam.cobia.config.spring;

import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.Protocol;
import lam.cobia.rpc.proxy.ProxyFactory;
import lam.cobia.spi.ServiceFactory;

/**
* <p>
* cobia reference
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class CobiaReference implements Reference{
	
	private Protocol protocol = ServiceFactory.takeDefaultInstance(Protocol.class);
	
	private ProxyFactory proxyFactory = ServiceFactory.takeDefaultInstance(ProxyFactory.class);
	
	@Override
	public <T> T refer(Class<T> clazz) {
		Consumer<T> consumer = protocol.refer(clazz);
		return proxyFactory.getConsumerProxy(consumer);
	}

}
