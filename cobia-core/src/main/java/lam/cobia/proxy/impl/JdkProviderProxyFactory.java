package lam.cobia.proxy.impl;

import lam.cobia.config.spring.CServiceBean;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.proxy.AbstractProviderProxyFactory;
import lam.cobia.rpc.AbstractProvider;
import lam.cobia.rpc.support.Provider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description: JdkProviderProxyFactory
 * @author: linanmiao
 * @date: 2018/7/20
 * @version: 1.0
 */
public class JdkProviderProxyFactory extends AbstractProviderProxyFactory {
    @Override
    public <T> Provider<T> getProvider(T ref, Class<T> clazz, CServiceBean<T> serviceBean) {
        Provider<T> provider = new AbstractProvider<T>(ref, clazz, serviceBean) {
            @Override
            protected Object doInvoke(T proxy, String method, Class<?>[] parameterTypes, Object[] arguments) {
                try {
                    Method m = proxy.getClass().getMethod(method, parameterTypes);
                    return m.invoke(proxy, arguments);
                } catch (NoSuchMethodException e) {
                    throw new CobiaException(e);
                } catch (SecurityException e) {
                    throw new CobiaException(e);
                } catch (IllegalAccessException e) {
                    throw new CobiaException(e);
                } catch (IllegalArgumentException e) {
                    throw new CobiaException(e);
                } catch (InvocationTargetException e) {
                    throw new CobiaException(e);
                }
            }
        };
        return provider;
    }
}
