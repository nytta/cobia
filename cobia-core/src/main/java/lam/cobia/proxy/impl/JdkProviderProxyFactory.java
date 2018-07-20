package lam.cobia.proxy.impl;

import lam.cobia.proxy.ProviderProxyFactory;
import lam.cobia.rpc.Provider;

/**
 * @description: @TODO
 * @author: linanmiao
 * @date: 2018/7/20
 * @version: 1.0
 */
public class JdkProviderProxyFactory implements ProviderProxyFactory {
    @Override
    public <T> Provider<T> getProvider(T ref, Class<T> clazz) {
        return null;
    }
}
