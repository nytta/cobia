package lam.cobia.proxy.impl;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.proxy.AbstractConsumerProxyFactory;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.DefaultInvocation;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Result;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: JdkConsumerProxyFactory
 * @author: linanmiao
 * @date: 2018/7/20
 * @version: 1.0
 */
public class JdkConsumerProxyFactory extends AbstractConsumerProxyFactory {

    @Override
    public <T> T getConsumerProxy(Consumer<T> consumer, Class<?>[] interfaces) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        T t = (T) Proxy.newProxyInstance(classLoader, interfaces, new DefaultInvocationHandler(consumer));
        return t;
    }

    private static class DefaultInvocationHandler implements InvocationHandler {

        private Consumer<?> consumer;

        public DefaultInvocationHandler(Consumer<?> consumer) {
            this.consumer = consumer;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Invocation invocation = new DefaultInvocation()
                    .setInterface(consumer.getInterface().getName())
                    .setMethod(method.getName())
                    .setParamenterTypes(method.getParameterTypes())
                    .setArguments(args);

            Result result = consumer.invoke(invocation);
            if (result == null) {
                throw new CobiaException("result == null");
            }
            return result.getValue();
        }

    }
}
