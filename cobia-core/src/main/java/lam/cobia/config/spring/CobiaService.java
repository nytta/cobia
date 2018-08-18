package lam.cobia.config.spring;

import java.util.ArrayList;
import java.util.List;

import lam.cobia.proxy.ProviderProxyFactory;
import lam.cobia.rpc.Exporter;
import lam.cobia.rpc.Protocol;
import lam.cobia.rpc.Provider;
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

	private ProviderProxyFactory providerProxyFactory = ServiceFactory.takeDefaultInstance(ProviderProxyFactory.class);
	
	private final List<Exporter<?>> exporters = new ArrayList<Exporter<?>>();
	
	@Override
	public <T> void export(T ref, Class<T> clazz) {
		Provider<T> provider = providerProxyFactory.getProvider(ref, clazz);
		Exporter<T> exporter = protocol.export(provider);
		exporters.add(exporter);
	}
	
}
