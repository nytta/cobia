package lam.cobia.proxy;

import lam.cobia.config.spring.CServiceBean;
import lam.cobia.rpc.support.Provider;

/**
 * @description: AbstractProviderProxyFactory
 * @author: linanmiao
 * @date: 2018/7/21 0:26
 * @version: 1.0
 */
public abstract class AbstractProviderProxyFactory implements ProviderProxyFactory{
    @Override
    public abstract <T> Provider<T> getProvider(T ref, Class<T> clazz, CServiceBean<T> serviceBean);
}
