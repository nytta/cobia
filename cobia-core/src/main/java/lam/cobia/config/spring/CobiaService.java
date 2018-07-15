package lam.cobia.config.spring;

import java.util.ArrayList;
import java.util.List;

import lam.cobia.rpc.Exporter;
import lam.cobia.rpc.Protocol;
import lam.cobia.rpc.Provider;
import lam.cobia.rpc.proxy.ProxyFactory;
import lam.cobia.spi.ServiceFactory;

/**
* <p>
* cobia service
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class CobiaService implements Service{
	
	private Protocol protocol = ServiceFactory.takeDefaultInstance(Protocol.class);
	
	private ProxyFactory proxyFactory = ServiceFactory.takeDefaultInstance(ProxyFactory.class);
	
	private final List<Exporter<?>> exporters = new ArrayList<Exporter<?>>();
	
	@Override
	public <T> void export(T ref, Class<T> clazz) {
		Provider<T> provider = proxyFactory.getProvider(ref, clazz);
		Exporter<T> exporter = protocol.export(provider);
		exporters.add(exporter);
	}
	
}
