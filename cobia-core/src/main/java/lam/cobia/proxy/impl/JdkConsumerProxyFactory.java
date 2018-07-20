package lam.cobia.proxy.impl;

import lam.cobia.proxy.ConsumerProxyFactory;
import lam.cobia.rpc.Consumer;

/**
 * @description: JdkConsumerProxyFactory
 * @author: linanmiao
 * @date: 2018/7/20
 * @version: 1.0
 */
public class JdkConsumerProxyFactory implements ConsumerProxyFactory {
    @Override
    public <T> T getConsumerProxy(Consumer<T> t) {

        return null;
    }
}
