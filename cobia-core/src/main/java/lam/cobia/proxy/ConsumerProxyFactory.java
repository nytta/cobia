package lam.cobia.proxy;

import lam.cobia.rpc.Consumer;

public interface ConsumerProxyFactory {

    public <T> T getConsumerProxy(Consumer<T> t);

}
