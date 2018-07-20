package lam.cobia.proxy;

import lam.cobia.rpc.Consumer;

/**
 * @description: AbstractConsumerProxyFactory
 * @author: linanmiao
 * @date: 2018/7/21 0:22
 * @version: 1.0
 */
public abstract class AbstractConsumerProxyFactory implements ConsumerProxyFactory{
    @Override
    public <T> T getConsumerProxy(Consumer<T> consumer) {
        Class<?>[] interfaces = new Class<?>[] {consumer.getInterface()};
        T t = getConsumerProxy(consumer, interfaces);
        return t;
    }

    public abstract <T> T getConsumerProxy(Consumer<T> consumer, Class<?>[] interfaces);
}
