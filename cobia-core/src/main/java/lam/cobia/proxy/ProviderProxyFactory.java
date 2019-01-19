package lam.cobia.proxy;

import lam.cobia.config.spring.CServiceBean;
import lam.cobia.rpc.support.Provider;
import lam.cobia.spi.Spiable;

@Spiable("jdk")
public interface ProviderProxyFactory {

    public <T> Provider<T> getProvider(T ref, Class<T> clazz, CServiceBean<T> serviceBean);

}
