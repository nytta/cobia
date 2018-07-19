package lam.cobia.proxy;

import lam.cobia.rpc.Provider;

public interface ProviderProxyFactory {

    public <T> Provider<T> getProvider(T ref, Class<T> clazz);

}
